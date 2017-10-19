//package tp1Contrainte;

//import instructor.CHOCO.masterthesisdefensetimetabling.MainApp;

import java.util.*;
import java.io.*;

import choco.cp.model.*;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.integer.*;
import choco.Choco;

class Item{
	int w,h;
	public Item(int w, int h){
		this.w = w; this.h = h;
	}
}
public class BPCHOCO {

	public int W, H;
	public int n;
	public int[] w;
	public int[] h;
	public int[] s_x;
	public int[] s_y;
	public int[] s_o;
	
	public BPCHOCO(){
		
	}
	public void readData(String fn){
		try{
			Scanner in = new Scanner(new File(fn));
			W = in.nextInt();
			H = in.nextInt();
			ArrayList<Item> I = new ArrayList<Item>();
			while(true){
				int wi = in.nextInt();
				if(wi == -1) break;
				int hi = in.nextInt();
				I.add(new Item(wi,hi));
			}
			in.close();
			
			n = I.size();
			w = new int[n];
			h = new int[n];
			for(int i = 0; i < I.size(); i++){
				w[i] = I.get(i).w;
				h[i] = I.get(i).h;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void solveWithOrientation(int timeLimit){
	
	
		timeLimit = timeLimit*1000;
		
		CPModel m = new CPModel();
		IntegerVariable[] x = new IntegerVariable[n];
		IntegerVariable[] y = new IntegerVariable[n];
		IntegerVariable[] o = new IntegerVariable[n];
		for (int i = 0; i <n; i++) {
			x[i] = Choco.makeIntVar("x[" + i + "]", 0, W);
			y[i] = Choco.makeIntVar("y[" + i + "]", 0, H);
			o[i] = Choco.makeIntVar("o[" + i + "]", 0, 1);
		}


		for (int i = 0; i <n - 1; i++) {
			for (int j = i + 1; j <n; j++) {
				//o[i] = 0, o[j] = 0
				m.addConstraint(Choco.implies(
						Choco.and(Choco.eq(o[i], 0), Choco.eq(o[j], 0)),
						Choco.or(Choco.leq(Choco.plus(x[j], w[j]), x[i]),
								Choco.leq(Choco.plus(x[i], w[i]), x[j]),
								Choco.leq(Choco.plus(y[i], h[i]), y[j]),
								Choco.leq(Choco.plus(y[j], h[j]), y[i]))));

		//		[...TODO...]
			}
		}

		CPSolver s = new CPSolver();
		s.read(m);
		s.setTimeLimit(timeLimit);
		//ChocoLogging.toSolution();
		Boolean ok = s.solve();

		System.out.println("solution    =  " + ok);
		
		s_x = new int[n];
		s_y = new int[n];
		s_o = new int[n];
		for(int i = 0; i < n; i++){
			s_x[i] = s.getVar(x[i]).getVal();
			s_y[i] = s.getVar(y[i]).getVal();
			s_o[i] = s.getVar(o[i]).getVal();
		}
		
		//System.out.println("  " + s.toString());
		
		
		for (int i = 0; i <n; i++) {
			System.out.println("Item " + i + " at (" + s.getVar(x[i]).getVal()
					+ "," + s.getVar(y[i]).getVal() + ")" + "  "
					+ s.getVar(o[i]).getVal());
		}
		

	}
	   public void outTableNew(String fn) {
	        final String[] Color = new String[]{
	                "#FFFF00", "#1CE6FF", "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
	                "#FF0000", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87",
	                "#5A0007", "#809693", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80",
	                "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100",
	                "#FFDBE5", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F",
	                "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
	                "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66",
	                "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C",
	                "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81",
	                "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00",
	                "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
	                "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329",
	                "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C",
	                "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800",
	                "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51",
	                "#C895C5", "#320033", "#FF6832", "#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58"
	        };
	        try {
	            File outFile = new File(fn);
	            PrintWriter out;
	            out = new PrintWriter(outFile);
	            out.println("<!doctype html>\n<html>\n<head>\n<title>Binpacking2D</title>\n</head>\n<body>\n");

	            boolean[] isIndex  = new boolean[n+2];

	            int size = 650 / (Math.max(W, H) + 1);
	            out.println("<style type=\"text/css\">\n" + "table, td {\n" +
	                            "\t\tborder : 1px solid black;\n" +
	                            "\t\tborder-collapse: collapse;text-align : center;\n" +
	                            "\t}\n" +
	                            "\ttd {\n" +
	                            "\t\twidth : +" + size + "px;\n" +
	                            "\t\theight: +" + size + "px;\n" +
	                            "\t}"
	            );
	            for (int i = 0; i < n; i++) {
	                out.println("td.class" + (i) + " { \n background-color:" + Color[i] + ";  \n}");
	            }
	            out.println("</style>");

	            out.println("<table>");
	            for (int i = 0; i <= H; i++) {
	                out.println("<tr>");
	                for (int j = 0; j <= W; j++) {
	                    if (i == 0) {
	                        if (j == 0) {
	                            out.print("<td>");
	                            out.println("</td>");
	                        } else {
	                            out.print("<td>");
	                            out.print(j);
	                            out.println("</td>");
	                        }
	                    } else {
	                        if (j == 0) {
	                            out.print("<td>");
	                            out.print(i);
	                            out.println("</td>");
	                        } else {
	                            boolean flag = false;
	                            for (int k = 0; k < n; k++) {
	                                int xk = s_x[k];
	                                int yk = s_y[k];
	                                int wk = w[k];
	                                int hk = h[k];
	                                if (s_o[k] == 0) {
	                                    if (j - 1 >= xk && j - 1 <= xk + wk - 1 && i - 1 >= yk && i - 1 <= yk + hk - 1) {
	                                        out.print("<td class='class" + k + "'>");

	                                        if(!isIndex[k] && (j-1)==(xk+xk+wk-1)/2 && (i-1)==(yk+yk+hk-1)/2){
	                                            out.print(k);
	                                            isIndex[k]=true;
	                                        }
	                                        flag = true;
	                                        break;
	                                    }
	                                } else {
	                                    if (j - 1 >= xk && j - 1 <= xk + hk - 1 && i - 1 >= yk && i - 1 <= yk + wk - 1) {
	                                        out.print("<td class='class" + k + "'>");
	                                        if(!isIndex[k] && (j-1)==(xk+xk+hk-1)/2 && (i-1)==(yk+yk+wk-1)/2){
	                                            out.print(k);
	                                            isIndex[k]=true;
	                                        }
	                                        flag = true;
	                                        break;
	                                    }
	                                }
	                            }
	                            if (flag) out.println("</td>");
	                            else {
	                                out.print("<td>");
	                                out.println("</td>");
	                            }
	                        }
	                    }

	                }
	                out.println("</tr>");
	            }
	            out.println("</table>");

	            out.println("</body></html>");
	            out.close();
	        } catch (IOException exx) {
	            exx.printStackTrace();
	        }
	    }

	public void outTable(String filename) {
		final String[] Color = new String[] { "#000000", "#FFFF00", "#1CE6FF",
				"#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059",
				"#FF0000", "#7A4900", "#0000A6", "#63FFAC", "#B79762",
				"#004D43", "#8FB0FF", "#997D87", "#5A0007", "#809693",
				"#1B4400", "#FEFFE6", "#4FC601", "#3B5DFF", "#4A3B53",
				"#FF2F80", "#61615A", "#BA0900", "#6B7900", "#00C2A0",
				"#FFAA92", "#FF90C9", "#B903AA", "#D16100", "#FFDBE5",
				"#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8",
				"#013349", "#00846F", "#372101", "#FFB500", "#C2FFED",
				"#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09",
				"#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75",
				"#B77B68", "#7A87A1", "#788D66", "#885578", "#FAD09F",
				"#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED",
				"#886F4C", "#34362D", "#B4A8BD", "#00A6AA", "#452C2C",
				"#636375", "#A3C8C9", "#FF913F", "#938A81", "#575329",
				"#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757",
				"#C8A1A1", "#1E6E00", "#7900D7", "#A77500", "#6367A9",
				"#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700",
				"#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF",
				"#99ADC0", "#3A2465", "#922329", "#5B4534", "#FDE8DC",
				"#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72",
				"#6A3A4C", "#83AB58", "#001C1E", "#D1F7CE", "#004B28",
				"#C8D0F6", "#A3A489", "#806C66", "#222800", "#BF5650",
				"#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4",
				"#1E0200", "#5B4E51", "#C895C5", "#320033", "#FF6832",
				"#66E1D3", "#CFCDAC", "#D0AC94", "#7ED379", "#012C58" };
		try {
			File outFile = new File(filename);
			PrintWriter out;
			out = new PrintWriter(outFile);
			out.println("<!doctype html>\n<html>\n<head>\n<title>Binpacking2D</title>\n</head>\n<body>\n");

			int size = 650 / (Math.max(W, H) + 1);
			out.println("<style type=\"text/css\">\n" + "table, td {\n"
					+ "\t\tborder : 1px solid black;\n"
					+ "\t\tborder-collapse: collapse;text-align : center;\n"
					+ "\t}\n" + "\ttd {\n" + "\t\twidth : +" + size + "px;\n"
					+ "\t\theight: +" + size + "px;\n" + "\t}");
			for (int i = 0; i < n; i++) {
				out.println("td.class" + (i) + " { \n background-color:"
						+ Color[i] + "; \n}");
			}
			out.println("</style>");

			out.println("<table>");
			for (int i = 0; i <= H; i++) {
				out.println("<tr>");
				for (int j = 0; j <= W; j++) {
					if (i == 0) {
						if (j == 0) {
							out.print("<td>");
							out.println("</td>");
						} else {
							out.print("<td>");
							out.print(j-1);
							out.println("</td>");
						}
					} else {
						if (j == 0) {
							out.print("<td>");
							out.print(i-1);
							out.println("</td>");
						} else {
							boolean flag = false;
							for (int k = 0; k < n; k++) {
								int xk = s_x[k];
								int yk = s_y[k];
								int wk = w[k];
								int hk = h[k];
								if (s_o[k] == 0) {
									if (j - 1 >= xk && j - 1 <= xk + wk - 1
											&& i - 1 >= yk
											&& i - 1 <= yk + hk - 1) {
										out.print("<td class='class" + k + "'>");
										out.print(k);
										flag = true;
										break;
									}
								} else {
									if (j - 1 >= xk && j - 1 <= xk + hk - 1
											&& i - 1 >= yk
											&& i - 1 <= yk + wk - 1) {
										out.print("<td class='class" + k + "'>");
										//out.print(k);
										flag = true;
										break;
									}
								}
							}
							if (flag)
								out.println("</td>");
							else {
								out.print("<td>");
								out.println("</td>");
							}
						}
					}

				}
				out.println("</tr>");
			}
			out.println("</table>");

			out.println("</body></html>");
			out.close();
		} catch (IOException exx) {
			exx.printStackTrace();
		}
	}

	public void testBatch(String filename, int nbTrials, int timeLimit) {
		BPCHOCO bp = new BPCHOCO();
		bp.readData(filename);
		double[] t = new double[nbTrials];
		double avg_t = 0;
		for (int k = 0; k < nbTrials; k++) {
			double t0 = System.currentTimeMillis();
			bp.solveWithOrientation(timeLimit);
			
			t[k] = (System.currentTimeMillis() - t0) * 0.001;
			avg_t += t[k];
		}
		avg_t = avg_t * 1.0 / nbTrials;
		System.out.println("Time = " + avg_t);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BPCHOCO bp = new BPCHOCO();
		BPCHOCO s = new BPCHOCO();
		//bp.readData("BinPacking2D\\bin-packing-2D-W10-H7-I6.txt");
		//bp.readData("/home/gg/workspace/ContrainteProgrm/src/tp1Contrainte/BinPacking2D/bin-packing-2D-W10-H7-I6.txt");
		bp.readData("/home/gg/workspace/ContrainteProgrm/src/tp1Contrainte/BinPacking2D/bin-packing-2D-W10-H7-I6.txt");
		bp.solveWithOrientation(100);
		bp.outTable("CHOCO-bin-packing-2D.html");
		bp.outTableNew("CHOCO-bin-packing-2dView.html");
		
		
		
		
		
		//BPCHOCO bp1 = new BPCHOCO();
		//bp1.testBatch("data\\BinPacking2D\\bin-packing-2D-W10-H7-I6.txt",1,300);
		
		
	}
	private void solve() {
		// TODO Auto-generated method stub
		
	}

}
