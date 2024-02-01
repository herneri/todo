/*
    Copyright 2023 Eric Hernandez

    This file is part of todo.

    todo is free software: you can redistribute it and/or modify it under the terms of the GNU General
    Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    todo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with todo. If not, see <https://www.gnu.org/licenses/>.
*/

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;

class Todo {
	public static final int DAY_NUM = 7;
	private static final String EDIT_SYMBOL = "<\\EDIT\\>";
	public static final String[] days = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
	public static String[] dayData = new String[DAY_NUM];

	public static void checkDir() {
		File dir = new File(TodoFile.TODO_PATH);
		if (!dir.exists()) {
			System.out.println("NOTICE: Todo directory not found, creating new one");
			dir.mkdir();
		}

		return;
	}

	public static void fileNotFoundPrompt(String file) {
		System.out.println("ERROR: File \"" + file + "\" can't be found");
		System.out.println("Would you like to create this file? [y/N]");

		Scanner errorInput = new Scanner(System.in);
		String choice = errorInput.nextLine();
		if (choice.equals("y")) {
			TodoFile.write(file);
		}

		return;
	}

	public static boolean isDay(String day) {
		for (int i = 0; i < DAY_NUM - 1; i++) {
			if (day.equals(days[i])) {
				return true;
			}
		}

		return false;
	}

	public static Queue<String> editParse(BufferedReader read, String day) {
		Queue<String> data = new LinkedList<String>();
		boolean isValue = false;
		String line = null;

		try {
			while ((line = read.readLine()) != null) {
				if (line.equals(day + ":")) {
					data.offer(EDIT_SYMBOL);
					isValue = true;
					continue;
				} else if (line.equals(";") && isValue == true) {
					isValue = false;
					data.offer(EDIT_SYMBOL);
					continue;
				}

				if (isValue == false) {
					data.offer(line);
				}
			}
		} catch (java.io.IOException e) {
			System.err.println("ERROR: Can't read file");
		}

		return data;
	}

	public static void editWrite(String file, Queue<String> data, String day) {
		LinkedList<String> content = new LinkedList<String>();
		Scanner input = new Scanner(System.in);
		BufferedWriter write = null;
		boolean isEdit = false;
		String text = null;

		System.out.println("Editing " + day + ":");
		content.offer(day + ":");

		while (true) {
			text = input.nextLine();
			if (text.equals(">E")) {
				content.offer(";");
				break;
			} else if (text.equals(">X")) {
				return;
			}

			content.offer(text);
		}

		try {
			write = new BufferedWriter(new FileWriter(file));

			while (data.peek() != null) {
				if (data.peek() == EDIT_SYMBOL && isEdit == false) {
					isEdit = true;

					while (content.peek() != null) {
						write.write(content.getFirst() + "\n");
						content.poll();
					}

					data.poll();
					continue;
				} else if (data.peek() == EDIT_SYMBOL && isEdit == true) {
					isEdit = false;
					data.poll();
					continue;
				}

				write.write(data.peek() + "\n");
				data.poll();
			}

			write.close();
		} catch (java.io.IOException e) {
			System.err.println("ERROR: Could not edit file");
		}

		return;
	}
}
