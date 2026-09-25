from litellm import completion
from typing import List, Dict
import sys
import json
import re
import os

agent_rules = [{
    "role": "system",
    "content": """
You are an AI agent that can perform tasks by using available tools.

Available tools:
- list_files() -> List[str]: List all files in the current directory.
- read_file(file_name: str) -> str: Read the content of a file.
- terminate(message: str): End the agent loop and print a summary to the user.

GUIDELINES:
- Do ONLY what the user asked. Do not perform extra actions 
  "to be helpful".
- Before choosing your next action, ask yourself: 
  "Has the user's request been fully satisfied?"
- If yes, call terminate with a summary.
- If no, choose the next tool to make progress.
- Do not read files unless the user explicitly asked you to 
  read them.

Every response MUST be a tool invocation in the action format.
Never respond in plain text.

```action
{
    "tool_name": "insert tool_name",
    "args": {...fill in any required arguments here...}
}
"""
}]

memory = []

max_iterations= 5


def generate_response(messages: List[Dict]) -> str:
    """Call LLM to get response"""
    response = completion(
        model="openai/gpt-4",
        messages=messages,
        max_tokens=1024
    )
    return response.choices[0].message.content


def parse_action(response: str) -> Dict:
    """Parse the LLM response into a structured action dictionary."""
    try:
        response = extract_markdown_block(response, "action")
        response_json = json.loads(response)
        if "tool_name" in response_json and "args" in response_json:
            return response_json
        else:
            return {"tool_name": "error", "args": {"message": "You must respond with a JSON tool invocation."}}
    except json.JSONDecodeError:
        return {"tool_name": "error", "args": {"message": "Invalid JSON response. You must respond with a JSON tool invocation."}}

def extract_markdown_block(response: str, block_type: str = "action") -> str:
    """
    Extrae el contenido de un bloque de código markdown del tipo especificado.

    Ejemplo:
        Entrada:  "```action\n{\"tool_name\": \"list_files\", \"args\": {}}\n```"
        Salida:   '{"tool_name": "list_files", "args": {}}'
    """
    # Patrón: ```action ... ```
    # - ``` seguido del tipo de bloque (ej: "action")
    # - Captura todo hasta el siguiente ```
    pattern = rf"```{block_type}\s*\n(.*?)\n?```"

    match = re.search(pattern, response, re.DOTALL)

    if match:
        return match.group(1).strip()
    else:
        # Si no encuentra el bloque, devuelve el texto original
        # para que json.loads falle y parse_action devuelva el error
        return response.strip()

def list_files() -> list[str]:
    """
    Lista todos los archivos en el directorio actual.
    Devuelve una lista de nombres (solo archivos, no carpetas).
    """
    return [f for f in os.listdir(".") if os.path.isfile(f)]


def read_file(file_name: str) -> str:
    """
    Lee el contenido de un archivo y lo devuelve como texto.
    Si el archivo no existe, devuelve un mensaje de error.
    """
    try:
        with open(file_name, "r", encoding="utf-8") as f:
            return f.read()
    except FileNotFoundError:
        return f"Error: el archivo '{file_name}' no existe."
    except Exception as e:
        return f"Error al leer '{file_name}': {e}"

def develop_custom_function():

  # Get user input for function description
  print("Your description: ", end='')
  function_description = input().strip()
   
  memory.append({
      "role": "user",
      "content": function_description
  })

  iterations = 1

   # The Agent Loop
  while iterations < max_iterations:

      prompt = agent_rules + memory

      # 2. Generate response from LLM
      print("Agent thinking...")
      response = generate_response(prompt)
      print(f"Agent response: {response}")

      # 3. Parse response to determine action
      action = parse_action(response)

      result = "Action executed"

      if action["tool_name"] == "list_files":
          result = {"result":list_files()}
      elif action["tool_name"] == "read_file":
          result = {"result":read_file(action["args"]["file_name"])}
      elif action["tool_name"] == "error":
          result = {"error":action["args"]["message"]}
      elif action["tool_name"] == "terminate":
          print(action["args"]["message"])
          break
      else:
          result = {"error":"Unknown action: "+action["tool_name"]}

      print(f"Action result: {result}")

      # 5. Update memory with response and results
      memory.extend([
          {"role": "assistant", "content": response},
          {"role": "user", "content": json.dumps(result)}
      ])

      # 6. Check termination condition
      if action["tool_name"] == "terminate":
          break

      iterations += 1



if __name__ == "__main__":

   develop_custom_function()