public class Main {
	private static String option;
	private static String file;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("ERROR: Provide option and file");
			return;
		}

		TodoFile.checkDir();
		option = args[0];
		file = args[1];
		
		switch (option) {
		case "r":
			TodoFile.read(file);
			break;
		case "n":
			TodoFile.write(file);
			break;
		case "d":
			TodoFile.delete(file);
			break;
		case "e":
			TodoFile.edit(file);
			break;
		default:
			System.err.printf("ERROR: Invalid argument \'%s\' \n", option);
			return;
		}

		return;
	}
}
