/*
	Copyright 2023 Eric Hernandez

	This file is part of todo.

	todo is free software: you can redistribute it and/or modify it under the terms of the GNU General
	Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

	todo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
	the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along with todo. If not, see <https://www.gnu.org/licenses/>.
*/

public class Main {
	private static String option;

	public static void main(String[] args) {
		if (args.length == 0 || (args.length == 1 && args[0].equals("h"))) {
			System.out.println("todo: usage: todo [option] [entry name]\n\nn - Create new entry\nr - Read entry\nd - Delete entry\ne - Edit entry\nh - Print options");
			return;
		}

		TodoFile.checkDir();
		option = args[0];

		if (args.length == 1 && option.equals("l")) {
			TodoFile.list();
			return;
		} else if (args.length < 2) {
			System.err.println("ERROR: Provide option and entry name");
			return;
		}

		TodoFile.name = args[1];

		switch (option) {
		case "r":
			TodoFile.read();
			break;
		case "n":
			TodoFile.write();
			break;
		case "d":
			TodoFile.delete();
			break;
		case "e":
			TodoFile.edit();
			break;
		default:
			System.err.printf("ERROR: Invalid argument \'%s\' \n", option);
			return;
		}

		return;
	}
}
