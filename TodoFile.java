/*
    Copyright 2023 Eric Hernandez

    This file is part of todo.

    todo is free software: you can redistribute it and/or modify it under the terms of the GNU General
    Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

    todo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
    the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with todo. If not, see <https://www.gnu.org/licenses/>.
*/

import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.util.Queue;
import java.util.LinkedList;

class TodoFile extends Todo {
	public static final String TODO_PATH = System.getenv("HOME") + ".todo";
	public static String name;

	public static void read() {
		try {
			BufferedReader f = new BufferedReader(new FileReader(TODO_PATH + "/" + TodoFile.name));
			Scanner input = new Scanner(System.in);
			String option = null;
			String s = null;

			System.out.print("Day to read: ");
			option = input.nextLine();

			boolean isValid = false;
			boolean foundSpecificValue = false;
			while ((s = f.readLine()) != null) {
				if (option.equals("*") || s.equals(option + ":")) {
					isValid = true;
					if (!option.equals("*")) {
						foundSpecificValue = true;
					}
				}

				if (s.equals(";")) {
					isValid = false;
					System.out.println();

					if (foundSpecificValue == true) {
						break;
					}

					continue;
				}

				if (isValid == true) {
					System.out.println(s);
				}
			}
		} catch (java.io.FileNotFoundException e) {
			fileNotFoundPrompt();
			return;
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}

		return;
	}

	public static void list() {
		File directory = new File(TODO_PATH);
		String[] entries = directory.list();
		int count = 0;

		for (int i = 0; i < entries.length; i++) {
			System.out.print(entries[i] + "\t");
			if (count == 3) {
				System.out.println();
				count = 0;
				continue;
			}

			count++;
		}

		System.out.println();
		return;
	}

	public static void write() {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(TODO_PATH + "/" + TodoFile.name));
			Scanner input = new Scanner(System.in);

			System.out.println(TODO_PATH + "/" + TodoFile.name);
			for (int i = 0; i < DAY_NUM; i++) {
				System.out.println("\n" + days[i] + ": ");
				while (true) {
					dayData[i] = input.nextLine(); 
					if (dayData[i].equals(">E")) {
						break;
					} else if (dayData[i].equals(">X")) {
						f.close();
						return;
					}
					// FIXME: Can't go back on later entries (Out of bounds)
					/*else if (dayData[i].equals(">B")) {
						if (i > 0) {
							i -= 2;
							break;
						}

						System.err.println("ERROR: Can't go back a spot");
						continue;
					}*/

					f.write(days[i] + ":\n");
					f.write(dayData[i] + "\n");
					f.write(";\n");
				}

			}

			f.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}

		return;
	}

	public static void delete() {
		File f = new File(TODO_PATH + "/" + TodoFile.name);
		
		if (!f.delete()) {
			System.out.println("ERROR: Failed to delete file " + f);
			return;
		}
		
		return;
	}

	public static void edit() {
		BufferedReader read;
		Scanner input = new Scanner(System.in);
		Queue<String> data = new LinkedList<String>();
		String day = null;

		try {
			read = new BufferedReader(new FileReader(TODO_PATH + "/" + TodoFile.name));
			
			if (read.readLine() == null) {
				System.err.println("NOTICE: File is empty, deleting file");
				delete();
				return;
			}
			read = new BufferedReader(new FileReader(TODO_PATH + "/" + TodoFile.name));
		} catch (java.io.FileNotFoundException e) {
			fileNotFoundPrompt();
			return;
		} catch (java.io.IOException e) {
			System.err.println("ERROR: Unable to write to file");
			return;
		}

		System.out.print("Enter day to edit: ");
		day = input.nextLine();
		if (isDay(day) == false) {
			System.err.printf("ERROR: '%s' is an invalid day\n", day);
			return;
		}
		
		if (day.equals("*")) {
			write();
			return;
		}

		data = editParse(read, day);
		editWrite(data, day);
		return;
	}
}
