import uuid
from datetime import datetime

inventory_db = {}

@register_tool(description="Analyze an image and describe what you see")
def process_inventory_image(action_context: ActionContext,
                            image_path: str) -> str:
    """
    Look at an image and describe the item, including type, condition, and notable features.
    Returns a natural language description.
    """
    with open(image_path, "rb") as image_file:
        image_data = base64.b64encode(image_file.read()).decode("utf-8")

    response = completion(
        model="gpt-4o",
        messages=[
            {
                "role": "user",
                "content": [
                    {
                        "type": "text",
                        "text": """Please describe this item for inventory purposes.
                        Include details about:
                        - What the item is
                        - Its key features
                        - The condition it's in
                        - Any visible wear or damage
                        - Anything notable about it"""
                    },
                    {
                        "type": "image",
                        "image_url": {
                            "url": f"data:image/jpeg;base64,{image_data}"
                        }
                    }
                ]
            }
        ],
        max_tokens=1000
    )

    return response

@register_tool(tags=["inventory"], description="Save an item to inventory")
def save_item(item_name: str, description: str, condition: str, estimated_value: float) -> dict:
        """Save a single item to the inventory database."""
        item_id = str(uuid.uuid4())
        inventory_db[item_id] = {
            "id": item_id,
            "name": item_name,
            "description": description,
            "condition": condition,
            "estimated_value": estimated_value,
            "added_date": datetime.now().isoformat()
        }
        return {"item_id": item_id}

@register_tool(tags=["inventory"], description="Get all inventory items")
def get_inventory() -> List[dict]:
        """Retrieve all items in the inventory."""
        return list(inventory_db.values())

@register_tool(tags=["inventory"], description="Get specific inventory item")
def get_item(item_id: str) -> dict:
        """Retrieve a specific inventory item."""
        return inventory_db.get(item_id)

@register_tool(tags=["system"], terminal=True, description="Terminate execution")
def terminate(message: str) -> str:
        """Terminate the agent's execution with a final message."""
        return f"{message}\nTerminating..."

    # ============ CELDA 4: AGENTE ============
SYSTEM_PROMPT = """You are an expert inventory manager.
    When shown items:
    1. Identify the item type and key features
    2. Assess condition from visual cues
    3. Estimate market value based on condition and features
    4. Maintain organized records with consistent descriptions

    Always be thorough in descriptions and conservative in value estimates."""

goals = [
        Goal(priority=0, name="system", description=SYSTEM_PROMPT),
        Goal(priority=1,
            name="inventory_management",
            description="""Maintain an accurate inventory of items including:
    - Detailed descriptions
    - Condition assessment
    - Value estimates
    - Historical tracking"""),
        Goal(priority=2,
            name="terminate",
            description="Call terminate when the inventory task is complete."),
    ]

agent = Agent(
        goals=goals,
        agent_language=AgentFunctionCallingActionLanguage(),
        action_registry=PythonActionRegistry(tags=["inventory", "system"]),
        generate_response=generate_response,
        environment=Environment(),
    )



#agent = Agent(
#    goals=goals,
#    agent_language=JSONAgentLanguage(),
#    action_registry=registry,
#    capabilities=[
#        SystemPromptCapability("""You are an expert inventory manager.
#        When shown items:
#        1. Identify the item type and key features
#        2. Assess condition from visual cues
#        3. Estimate market value based on condition and features
#        4. Maintain organized records with consistent descriptions
        
#        Always be thorough in descriptions and conservative in value estimates.""")
#    ]
#)

    # ============ CELDA 5: RUN ============
user_input = """I have a pair of Air Jordan basketball shoes.
                     They're red with the Jumpman logo, showing some wear
                     and slight discoloration."""

final_memory = agent.run(user_input)
for m in final_memory.get_memories():
        print(m)
        print("---")


print(inventory_db)