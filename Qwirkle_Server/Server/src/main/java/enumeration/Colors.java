package enumeration;

public enum Colors {
	Red, //1
	Blue, //2
	Yellow, //3
	Orange, //4
	Purple, //5
	Green, //6
	Black, //7
	White, //8
	Dark_Blue, //9
	Magenta,//10
	Skyblue, //11
	Brown,
	Dark_Green;//12
	
	public static Colors fromInteger(int x) {
		switch(x) {
		case(0):
			return Red;
		case(1):
			return Blue;
		case(2):
			return Yellow;
		case(3):
			return Orange;
		case(4):
			return Purple;
		case(5):
			return Green;
		case(6):
			return Black;
		case(7):
			return White;
		case(8):
			return Dark_Blue;
		case(9):
			return Magenta;
		case(10):
			return Skyblue;
		case(11):
			return Brown;
		case(12):
			return Dark_Green;
		}
		return null;
	}

}

