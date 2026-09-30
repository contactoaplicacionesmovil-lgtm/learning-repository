    # Define the agent's goals
    goals = [
        Goal(priority=1,
             name="Gather Information",
             description="Read each file in the project in order to build a deep understanding of the project in order to write a README"),
        Goal(priority=1,
             name="Terminate",
             description="Call terminate when done and provide a complete README for the project in the message parameter")
    ]


    # First, we'll define our tools using decorators
    @register_tool(tags=["file_operations", "read"])
    def read_project_file(name: str) -> str:
        """Reads and returns the content of a specified project file.

        Opens the file in read mode and returns its entire contents as a string.
        Raises FileNotFoundError if the file doesn't exist.

        Args:
            name: The name of the file to read

        Returns:
            The contents of the file as a string
        """
        with open(name, "r") as f:
            return f.read()

    @register_tool(tags=["file_operations", "list"])
    def list_project_files() -> List[str]:
        """Lists all files in the current project directory.

        Scans the current directory and returns a sorted list of all files

        Returns:
            A sorted list of filenames
        """

        return sorted([file for file in os.listdir(".")])

    @register_tool(tags=["system"], terminal=True)
    def terminate(message: str) -> str:
        """Terminates the agent's execution with a final message.

        Args:
            message: The final message to return before terminating

        Returns:
            The message with a termination note appended
        """
        return f"{message}\nTerminating..."

    # Create an agent instance with tag-filtered actions
    agent = Agent(
        goals=goals,
        agent_language=AgentFunctionCallingActionLanguage(),
        # The ActionRegistry now automatically loads tools with these tags
        action_registry= PythonActionRegistry(tags=["file_operations", "system"]),
        generate_response=generate_response,
        environment=Environment()
    )


    # Run the agent with user input
    user_input = "Write a README for this project."
    final_memory = agent.run(user_input)
    # ===== DEBUG =====
    print("\n=== TOOLS ===")
    print(tools)

    print("\n=== TOOLS_BY_TAG ===")
    print(tools_by_tag)

    #print(final_memory.get_memories())
