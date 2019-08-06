package enumeration;

public enum Colors {
	Red, //0
	Blue, //1
	Yellow, //2
	Orange, //3
	Purple, //4
	Green, //5
	Black, //6
	White, //7
	Dark_Blue, //8
	Magenta, //9
	Skyblue, //10
	Brown, //11
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

