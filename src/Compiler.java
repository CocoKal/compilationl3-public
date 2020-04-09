import sa.Sa2Xml;
import sa.SaExp;
import sc.parser.*;
import sc.lexer.*;
import java.io.*;
import sa.SaNode;
import java.util.ArrayList;
import java.util.List;
import sc.node.*;
import ts.Ts;
//import sa.*;
//import ts.*;
//import c3a.*;
//import nasm.*;
//import fg.*;

public class Compiler {

	public static void main(String[] args) {

		String base = null;
		PushbackReader pushbackReader = null;
		List<String> files = new ArrayList<>();
		File folder = new File("test\\input");
		File[] filesList = folder.listFiles();

		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].getName().endsWith(".l") && filesList[i].isFile()) {
				files.add(filesList[i].getAbsolutePath());
			}
		}

		try {
			for (String file : files) {

				pushbackReader = new PushbackReader(new FileReader(file));
				base = removeSuffix(file, ".l");

				try {
					System.out.println(file);
					Parser p = new Parser(new Lexer(br));
					Start tree = p.parse();
					System.out.println("[SC]");
					tree.apply(new Sc2Xml(baseName));

					System.out.println("[SA]");
					Sc2sa sc2sa = new Sc2sa();
					tree.apply(sc2sa);
					SaNode saRoot = sc2sa.getRoot();
					new Sa2Xml(saRoot, base);
					System.out.println("Fin  ");


					System.out.println("[TABLE SYMBOLES]");
					Ts table = new Sa2ts(saRoot).getTableGlobale();
					table.afficheTout(base);


				} catch (Exception e) {
					System.out.println("Catch");
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static String removeSuffix(final String s, final String suffix) {
		if (suffix != null && s != null && s.endsWith(suffix)) {
			return s.substring(0, s.length() - suffix.length());
		}
		return s;
	}
    
}
