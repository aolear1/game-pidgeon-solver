# Makefile for Game Pidgeon Solver

# Variables
JAVAC = javac
JAVA = java
JFLAGS = -g 
SRC_DIR = src
BIN_DIR = bin
SCALE_FLAG = -Dsun.java2d.uiScale=2
MAIN_CLASS = GUI  # Main class with the main method

# Find all the Java source files, including subdirectories
SOURCES = $(wildcard $(SRC_DIR)/wordhunt/*.java) $(wildcard $(SRC_DIR)/trie/*.java) $(SRC_DIR)/GUI.java

# Automatically create a list of class files from the source files
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target: compile and run the project
all: $(BIN_DIR) $(CLASSES)
	$(JAVA) $(SCALE_FLAG) -cp $(BIN_DIR) $(MAIN_CLASS)

# Rule to create the bin directory if it doesn't exist
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Rule to compile each Java source file into a class file
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JAVAC) $(JFLAGS) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $<

# Clean the bin directory (remove compiled files)
clean:
	rm -rf $(BIN_DIR)/*

