import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

public class TodoFile {
	// FIXME: TODO_DIR is temporary, use TODO_PATH and fix issue with using home dir
	private static final String TODO_DIR = "todo";
	private static final String TODO_PATH = "~/" + TODO_DIR;
	private static final String EDIT_SYMBOL = "<\\EDIT\\>";

	public static void checkDir() {
		File dir = new File(TODO_DIR);
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
			write(file);
		}

		return;
	}

	public static void read(String file) {
		try {
			BufferedReader f = new BufferedReader(new FileReader(TODO_DIR + "/" + file));
			Scanner input = new Scanner(System.in);
			String option = null;
			String s = null;

			System.out.print("Day to read: ");
			option = input.nextLine();

			boolean isValid = false;
			while ((s = f.readLine()) != null) {
				if (option.equals("*") || s.equals(option + ":")) {
					isValid = true;
				} else if (s.equals(";")) {
					isValid = false;
				}

				if (isValid == true) {
					System.out.println(s);
				}
			}
		} catch (java.io.FileNotFoundException e) {
			fileNotFoundPrompt(file);
			return;
		} catch (Exception e) {
			System.out.println(e);
		}

		return;
	}

	public static void write(String file) {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(TODO_DIR + "/" + file));
			Scanner input = new Scanner(System.in);

			System.out.println(TODO_DIR + "/" + file);
			for (int i = 0; i < Week.DAY_NUM; i++) {
				System.out.println("\n" + Week.days[i] + ": ");
				while (true) {
					Week.dayData[i] = input.nextLine(); 
					if (Week.dayData[i].equals(">E")) {
						break;
					} else if (Week.dayData[i].equals(">X")) {
						f.close();
						return;
					}
					// FIXME: Can't go back on later entries (Out of bounds)
					/*else if (Week.dayData[i].equals(">B")) {
						if (i > 0) {
							i -= 2;
							break;
						}

						System.err.println("ERROR: Can't go back a spot");
						continue;
					}*/

					f.write(Week.days[i] + ":\n");
					f.write(Week.dayData[i] + "\n");
					f.write(";\n");
				}

			}

			f.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}

		return;
	}

	public static void delete(String file) {
		File f = new File(TODO_DIR + "/" + file);
		
		if (!f.delete()) {
			System.out.println("ERROR: Failed to delete file " + f);
			return;
		}
		
		return;
	}

	public static void edit(String file) {
		BufferedReader read;
		BufferedWriter write;
		Scanner input = new Scanner(System.in);
		String day = null;
		String line = null;
		Queue<String> data = new LinkedList<String>();

		try {
			read = new BufferedReader(new FileReader(TODO_DIR + "/" + file));
			
			if ((line = read.readLine()) == null) {
				System.err.println("NOTICE: File is empty, deleting file");
				return;
			}
			line = null;
			read = new BufferedReader(new FileReader(TODO_DIR + "/" + file));
		} catch (java.io.FileNotFoundException e) {
			fileNotFoundPrompt(file);
			return;
		} catch (java.io.IOException e) {
			System.err.println("ERROR: Unable to write to file");
			return;
		}


		System.out.print("Enter day to edit: ");
		day = input.nextLine();
		
		if (day.equals("*")) {
			write(file);
			return;
		}

		try {
			boolean isValue = false;
			while ((line = read.readLine()) != null) {
				if (line.equals(day + ":")) {
					data.offer(EDIT_SYMBOL + day);
					isValue = true;
					continue;
				} else if (line.equals(";") && isValue == true) {
					isValue = false;
					data.offer(EDIT_SYMBOL);
					continue;
				}

				data.offer(line);
			}
		} catch (java.io.IOException e) {
			System.err.println("ERROR: Can't read file");
		}

		System.out.println(data);
		return;
	}
}
