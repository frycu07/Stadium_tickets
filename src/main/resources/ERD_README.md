# Entity Relationship Diagram (ERD) for Stadium Tickets System

This directory contains an Entity Relationship Diagram (ERD) for the Stadium Tickets System in PlantUML format.

## Overview

The ERD represents the database schema for the Stadium Tickets System, showing all entities, their attributes, and the relationships between them.

## Entities

The system consists of the following main entities:

1. **Stadium** - Represents a physical stadium with a fixed capacity
2. **Match** - Represents a sporting event between two teams
3. **Ticket** - Represents a ticket for a specific seat at a specific match
4. **User** - Represents a system user with authentication details
5. **Role** - Represents user permissions (e.g., ADMIN, USER)

## Relationships

- One Stadium can host many Matches (One-to-Many)
- One Match can have many Tickets (One-to-Many)
- Users can have multiple Roles, and Roles can be assigned to multiple Users (Many-to-Many)

## How to View the ERD

The ERD is created using PlantUML, which is a text-based diagramming tool. To view the diagram:

1. **Online PlantUML Server**:
   - Visit [PlantUML Online Server](https://www.plantuml.com/plantuml/uml/)
   - Copy and paste the content of `erd_diagram.puml` into the text area
   - The diagram will be rendered automatically

2. **Using IntelliJ IDEA with PlantUML plugin**:
   - Install the PlantUML Integration plugin in IntelliJ IDEA
   - Open the `erd_diagram.puml` file
   - Click on the PlantUML tab at the bottom of the editor to view the diagram

3. **Using VS Code with PlantUML extension**:
   - Install the PlantUML extension in VS Code
   - Open the `erd_diagram.puml` file
   - Press Alt+D to preview the diagram

4. **Command Line**:
   - Install PlantUML JAR file
   - Run: `java -jar plantuml.jar erd_diagram.puml`
   - This will generate an image file in the same directory

## Modifying the ERD

If you need to modify the ERD:

1. Edit the `erd_diagram.puml` file
2. Follow the PlantUML syntax for entity-relationship diagrams
3. Use the viewing methods above to see your changes

## PlantUML Resources

- [PlantUML Official Website](https://plantuml.com/)
- [PlantUML Entity-Relationship Diagram Documentation](https://plantuml.com/ie-diagram)