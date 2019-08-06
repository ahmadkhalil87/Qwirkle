package enumeration;

public enum Shapes {
	Circle, //0
	Square, //1
	Triangle, //2
	Pentagon, //3
	Hexagon, //4
	Line, //5
	Hollow_Circle, //6
	Star, //7
	Six_Dot, //8
	Cross,//9
	X_Shape, //10
	Plus; //11
	
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
