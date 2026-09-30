import inspect
from typing import get_type_hints

# Registro principal: nombre de tool -> metadata
tools = {}

# Índice secundario: tag -> lista de nombres de tools
tools_by_tag = {}

def register_tool(tool_name=None, description=None, 
                 parameters_override=None, terminal=False, tags=None):
    """
      A decorator to dynamically register a function in the tools dictionary with its parameters, schema, and docstring.

      Parameters:
          tool_name (str, optional): The name of the tool to register. Defaults to the function name.
          description (str, optional): Override for the tool's description. Defaults to the function's docstring.
          parameters_override (dict, optional): Override for the argument schema. Defaults to dynamically inferred schema.
          terminal (bool, optional): Whether the tool is terminal. Defaults to False.
          tags (List[str], optional): List of tags to associate with the tool.

      Returns:
          function: The wrapped function.
    """
    def decorator(func):
        # Extract all metadata from the function
        metadata = get_tool_metadata(
            func=func,
            tool_name=tool_name,
            description=description,
            parameters_override=parameters_override,
            terminal=terminal,
            tags=tags
        )
        
        # Register in our global tools dictionary
        tools[metadata["tool_name"]] = {
            "description": metadata["description"],
            "parameters": metadata["parameters"],
            "function": metadata["function"],
            "terminal": metadata["terminal"],
            "tags": metadata["tags"]
        }
        
        # Also maintain a tag-based index
        for tag in metadata["tags"]:
            if tag not in tools_by_tag:
                tools_by_tag[tag] = []
            tools_by_tag[tag].append(metadata["tool_name"])
        
        return func
    return decorator


def get_tool_metadata(func, tool_name=None, description=None, 
                     parameters_override=None, terminal=False, tags=None):
    """
      Extracts metadata for a function to use in tool registration.

      Parameters:
          func (function): The function to extract metadata from.
          tool_name (str, optional): The name of the tool. Defaults to the function name.
          description (str, optional): Description of the tool. Defaults to the function's docstring.
          parameters_override (dict, optional): Override for the argument schema. Defaults to dynamically inferred schema.
          terminal (bool, optional): Whether the tool is terminal. Defaults to False.
          tags (List[str], optional): List of tags to associate with the tool.

      Returns:
          dict: A dictionary containing metadata about the tool, including description, args schema, and the function.
    """
    
    # Use function name if no tool_name provided
    tool_name = tool_name or func.__name__
    
    # Use docstring if no description provided
    description = description or (func.__doc__.strip() 
                                if func.__doc__ else "No description provided.")
    
    # If no parameter override, analyze the function
    if parameters_override is None:
        signature = inspect.signature(func)
        type_hints = get_type_hints(func)
        
        # Build JSON schema for arguments
        args_schema = {
            "type": "object",
            "properties": {},
            "required": []
        }
        
        # Examine each parameter
        for param_name, param in signature.parameters.items():
            # Skip special parameters
            if param_name in ["action_context", "action_agent"]:
                continue

            # Convert Python types to JSON schema types
            param_type = type_hints.get(param_name, str)
            param_schema = {
                "type": get_json_type(param_type)
            }
            
            args_schema["properties"][param_name] = param_schema
            
            # If parameter has no default, it's required
            if param.default == inspect.Parameter.empty:
                args_schema["required"].append(param_name)
    else:
        args_schema = parameters_override
    
    return {
        "tool_name": tool_name,
        "description": description,
        "parameters": args_schema,
        "function": func,
        "terminal": terminal,
        "tags": tags or []
    }


def get_json_type(python_type):
    """Convierte un tipo de Python a un tipo de JSON Schema."""
    type_mapping = {
        str:   "string",
        int:   "integer",
        float: "number",
        bool:  "boolean",
        list:  "array",
        dict:  "object",
        type(None): "null",
    }
    return type_mapping.get(python_type, "string")

class PythonActionRegistry(ActionRegistry):
    def __init__(self, tags: List[str] = None, tool_names: List[str] = None):
          super().__init__()

          self.terminate_tool = None

          for tool_name, tool_desc in tools.items():
              if tool_name == "terminate":
                  self.terminate_tool = tool_desc

              if tool_names and tool_name not in tool_names:
                  continue

              tool_tags = tool_desc.get("tags", [])
              if tags and not any(tag in tool_tags for tag in tags):
                  continue

              self.register(Action(
                  name=tool_name,
                  function=tool_desc["function"],
                  description=tool_desc["description"],
                  parameters=tool_desc.get("parameters", {}),
                  terminal=tool_desc.get("terminal", False)
              ))

    def register_terminate_tool(self):
        if self.terminate_tool:
            self.register(Action(
                name="terminate",
                function=self.terminate_tool["function"],
                description=self.terminate_tool["description"],
                parameters=self.terminate_tool.get("parameters", {}),
                terminal=self.terminate_tool.get("terminal", False)
            ))
        else:
            raise Exception("Terminate tool not found in tool registry")



