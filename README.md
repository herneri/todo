# todo

Create and manage multiple weekly todo lists from the command line.

## Usage

`todo [OPTION] [ENTRY_NAME]`

## Options

1. `n`	Create a new week list
2. `r`	Read an entry
3. `e`	Edit a specific day or all days
4. `d`	Delete an entry
5. `l`	List all entries

## Entry Creation

When creating an entry with the `n` option, there will be prompts
for each day of the week. (i.e. `sun:`) You are then able to write all tasks/data
for that specific day.

### Writing Data to a Day

When you're done with a day, enter the `>E` command to move on to the next one.

If you would like to stop writing an entry all together, use the `>X` command.

## Installation

Type the following command to install on Unix-like systems:
	`$ sudo make install`
