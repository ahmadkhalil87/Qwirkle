package enumeration;

public enum Shapes {
	Circle, //1
	Square, //2
	Triangle, //3
	Pentagon, //4
	Hexagon, //5
	Line, //6
	Hollow_Circle, //7
	Star, //8
	Six_Dot, //9
	Cross,//10
	X_Shape, //11
	Plus; //12
	
	public static Shapes fromInteger(int x) {
		switch(x) {
		case(0):
			return Circle;
		case(1):
			return Square;
		case(2):
			return Triangle;
		case(3):
			return Pentagon;
		case(4):
			return Hexagon;
		case(5):
			return Line;
		case(6):
			return Hollow_Circle;
		case(7):
			return Star;
		case(8):
			return Six_Dot;
		case(9):
			return Cross;
		case(10):
			return X_Shape;
		case(11):
			return Plus;
		}
		return null;
	}

}
