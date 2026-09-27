from litellm import completion
from typing import List, Dict


def generate_response(messages: List[Dict]) -> str:
    """Call LLM to get response"""
    response = completion(
        model="openai/gpt-4o",
        messages=messages,
        max_tokens=1024
    )
    return response.choices[0].message.content


#first asking
que_quieres_hacer = input("¿Qué función deseas crear?")

messages = [
    {"role": "system", "content": " Eres un ingeniero de sistemas con experiencia y vas a proporcionar funciones de Java básicas. Se breve con tus respuestas sin dejar de ser amigable"},
    {"role": "user", "content": que_quieres_hacer}
]

response1 = generate_response(messages)
print(response1)


# second asking
messages = [
   {"role": "system", "content": " Eres un ingeniero de sistemas con experiencia y vas a proporcionar funciones de Java básicas. Se breve con tus respuestas sin dejar de ser amigable"},
   {"role": "user", "content": que_quieres_hacer},

   {"role": "assistant", "content": response1},
   
   {"role": "user", "content": "add comprehensive documentation including: Function description, Parameter descriptions, Return value description, Example usage, Edge cases"}

]

response2 = generate_response(messages)
print(response2)

# third asking
messages = [
   {"role": "system", "content": " Eres un ingeniero de sistemas con experiencia y vas a proporcionar funciones de Java básicas. Se breve con tus respuestas sin dejar de ser amigable"},
   {"role": "user", "content": que_quieres_hacer},

   {"role": "assistant", "content": response1},

   {"role": "user", "content": "add comprehensive documentation including: Function description, Parameter descriptions, Return value description, Example usage, Edge cases"},

   {"role": "assistant", "content": response2},

   {"role": "user", "content": "add test cases using Java's unittest framework: Tests should cover: Basic functionality, Edge cases, Error cases, Various input scenarios"}

]

response3 = generate_response(messages)
print(response3)