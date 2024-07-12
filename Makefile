JC = javac
JVM = java
DIR = /usr/local/bin/todo

default: Main.java
	@$(JC) $^
	@$(JVM) Main

install: Main.java
	$(JC) $^
	mkdir $(DIR)
	touch $(DIR)/Manifest.txt
	echo "Main-class: Main" > $(DIR)/Manifest.txt
	jar -cfm $(DIR)/todo.jar $(DIR)/Manifest.txt Main.class Todo.class TodoFile.class 
	echo "alias todo=\"java -jar $(DIR)/todo.jar\"" >> ~/.bashrc

uninstall: $(DIR)/todo.jar $(DIR)/Manifest.txt
	rm -r $(DIR)

clean: Main.class
	rm *.class
