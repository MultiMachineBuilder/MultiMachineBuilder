# MultiMachineBuilder [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=MultiMachineBuilder_MultiMachineBuilder&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=MultiMachineBuilder_MultiMachineBuilder)
Multi Machine Builder is a computer game written in Java that lets you build machines.

## Repository and contribution tips
* Suggest new features in the issues
* Report bugs in the issues
* Submit bugfixes and new code in pull requests
* Submit your creations and feedback in discussions

## Game development setup
This project uses [Eclipse](https://www.eclipse.org/ide/) for development.
Plugins:
* WindowBuilder
* SonarLint
* ResourceBundle editor
Connect SonarLint to SonarCloud [here](https://sonarcloud.io/project/configuration?id=MultiMachineBuilder_MultiMachineBuilder) for rules to be correct
SonarCloud organization: multimachinebuilder

## Modding
1. Make a project in your IDE
2. Add the game directory into a project
3. Add game JAR as dependency
4. Create a class which implements `mmb.MODS.AddonCentral`
5. Implement all required methods
6. Add mod information into your AddonCentral
7. Add more classes
8. Add content into resources directory
9. Test your mod
10. Export your mod
11. Publish your mod and post it in the discussions

## How to use
* Create a world by pressing 'New world' button
* Specify the chunk size, world size, name and type
* Open the world. You may enable creative mode
* Check out various blocks. More information coming soon

### There are several major categories of blocks and items:
* Construction blocks (overlaps with materials)
* WireWorld gates and wires
* Materials (overlaps with construction blocks)
* Crafting parts
* Machine blocks
* Electrical wiring (to be redone)
* Crafting tables
* (MOD) Hello World
* And may others

### How to use mods
1. Link a mod from [Settings/External mods]
or copy it to mods directory [Mods/Open mods directory]
2. Read the instructions on the mod's homepage

### Creative tips
* Use the copy tool to quickly build repeating sections
* Make anything you like in Creative Mode. Good for testing potential survival machines

### Survival tips
1. Mine wood logs first
2. Make a crafting table and a pickaxe workbench
3. Make a wooden pickaxe
4. Mine some stone and Rudimentary Ore
5. Make a furnace<br>Make tree(s)
6. Smelt Rudimentary Ore to Rudimentary Ingots and some to Medium rudimentary wire
7. Make ULV Furnace Generator and ULV Electric Furnace<br>Make Rudimentary Pickaxe<br>Mine coal
8. Make Crusher to increase ore output
9. Make Cluster Mill, Wiremill, Material Combiner and Material Splitter
10. Mine gold, silver, copper, iron, nickel;
11. Make Machine Assembler
12. Make machine parts
13. Make VLV Furnace Generator and VLV Electric Furnace
14. Make VLV Alloy Smelter<br>Mine tin, zinc and chrome
15. Smelt Bronze, Brass, Steel and Nichrome
16. Make some VLV machines
17. ...

[WireWorld](https://github.com/MultiMachineBuilder/MultiMachineBuilder/blob/master/WireWorld.md)
Check Out [SSTO Project](https://spacedock.info/mod/2417/SSTO%20Project) and its [LPG addon](https://spacedock.info/mod/3022/SSTO%20Project%20LPG)
Check out information about Russian invasion of Ukraine [here](https://github.com/MultiMachineBuilder/MultiMachineBuilder/blob/master/RUSSIAN INVASION ON UKRAINE.md)
