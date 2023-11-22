import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

class TodoFile extends Todo {
	// FIXME: TODO_DIR is temporary, use TODO_PATH and fix issue with using home dir
	public static final String TODO_DIR = "todo";
	public static final String TODO_PATH = "~/" + TODO_DIR;

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
		Scanner input = new Scanner(System.in);
		Queue<String> data = new LinkedList<String>();
		String day = null;

		try {
			read = new BufferedReader(new FileReader(TODO_DIR + "/" + file));
			
			if (read.readLine() == null) {
				System.err.println("NOTICE: File is empty, deleting file");
				delete(file);
				return;
			}
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
		if (isDay(day) == false) {
			System.err.printf("ERROR: '%s' is an invalid day\n", day);
			return;
		}
		
		if (day.equals("*")) {
			write(file);
			return;
		}

		data = editParse(read, day);
		editWrite(TODO_DIR + "/" + file, data, day);
		return;
	}
}
