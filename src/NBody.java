import sun.nio.cs.ext.MacThai;

import java.util.InputMismatchException;

/**
 * Created by TinTin on 10/13/16.
 * This class process the velocity
 * and position of the planets with
 * respect to one another
 */
public class NBody {

    private static double[] px = new double[5];
    private static double[] py = new double[5];
    private static double[] vx = new double[5];
    private static double[] vy = new double[5];
    private static double[] m = new double[5];
    private static String[] image = new String[5];
    private static double R;
    private static double G = 6.67e-11;

    public static void main(String[] args) {
        In inStream = new In("text/planets.txt");
        boolean b = inStream.isEmpty();
        int n = inStream.readInt();
        int i = 0;
        int k = 0;
        R = inStream.readDouble();



        while (!b) {


            try {
                if (i == 0)
                    px[k] = inStream.readDouble();
                else if (i == 1)
                    py[k] = inStream.readDouble();
                else if (i == 2)
                    vx[k] = inStream.readDouble();
                else if (i == 3)
                    vy[k] = inStream.readDouble();
                else if (i == 4)
                    m[k] = inStream.readDouble();
                else
                    image[k] = inStream.readString();
            } catch(InputMismatchException e) {
                image[k] = inStream.readString();
            }


            i++;
            if (i == 6) {
                k++;
                i = 0;
            }

            b = inStream.isEmpty();
        }

        // Position all the planets and constitute movement of all planets
        drawPlanets(Double.parseDouble(args[0]), Double.parseDouble(args[1]));

        for (int p = 0; i < p; i++) {
            System.out.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    px[p], py[p], vx[p], vy[p], m[p], image[p]);
        }

    }

    private static void drawPlanets(double t, double dt) {
//        double ex = px[0], ey = py[0];
//        double mx = px[1], my = py[1];
//        double mex = px[2], mey = py[2];
//        double sx = px[3], sy = py[3];
//        double vx = px[4], vy = py[4];
        double deltaT = dt;

        // set the scale of the coordinate system
        StdDraw.setXscale(-R, R);
        StdDraw.setYscale(-R, R);

        do  {
            StdDraw.filledSquare(0.0, 0.0, 3.0);

            StdDraw.picture(0,0, "images/starfield.jpg");
            StdDraw.picture(px[0],  py[0], "images/" + image[0]);
            StdDraw.picture(px[1],  py[1], "images/" + image[1]);
            StdDraw.picture(px[2],  py[2], "images/" + image[2]);
            StdDraw.picture(px[3],  py[3], "images/" + image[3]);
            StdDraw.picture(px[4],  py[4], "images/" + image[4]);
            StdDraw.show(20);



            // Pairwise force, dx, dy and acceleration between sun and venus
            double[] forceOfSunAndVenus = pairWiseForce(px[4], py[4], vx[4], vy[4], m[4], px[3], py[3], vx[3], vy[3], m[3]);    // Return x-force and y-force
            vx[4] = vx[4] + dt * forceOfSunAndVenus[0] / m[4];
            vy[4] = vy[4] + dt * forceOfSunAndVenus[1] / m[4];

            // Pairwise force between sun and mercury
            double[] foreOfSunAndMercury = pairWiseForce(px[2], py[2], vx[2], vy[2], m[2], px[3], py[3], vx[3], vy[3], m[3]);
            vx[2] = vx[2] + dt * foreOfSunAndMercury[0] / m[2];
            vy[2] = vy[2] + dt * foreOfSunAndMercury[1] / m[2];



            // Pairwise force for sun and earth
            double[] forceOfSunAndEarth = pairWiseForce(px[0], py[0], vx[0], vy[0], m[0], px[3], py[3], vx[3], vy[3], m[3]);
            vx[0] = vx[0] + dt * forceOfSunAndEarth[0] / m[0];
            vy[0] = vy[0] + dt * forceOfSunAndEarth[1] / m[0];


            // Pairwise force between sun and mar
            double[] foreOfSunAndMar = pairWiseForce(px[1], py[1], vx[1], vy[1], m[1], px[3], py[3], vx[3], vy[3], m[3]);
            vx[1] = vx[1] + dt * foreOfSunAndMar[0] / m[1];
            vy[1] = vy[1] + dt * foreOfSunAndMar[1] / m[1];





            // New position for earth
            px[0] = px[0] + dt * vx[0];
            py[0] = py[0] + dt * vy[0];

            // New position for mar
            px[1] = px[1] + dt * vx[1];
            py[1] = py[1] + dt * vy[1];

            // New position for mercury
            px[2] = px[2] + dt * vx[2];
            py[2] = py[2] + dt * vy[2];


            // New position for venus
            px[4] = px[4] + dt * vx[4];
            py[4] = py[4] + dt * vy[4];

            deltaT = deltaT + dt;
        } while (deltaT < t);

    }

    private static double[] pairWiseForce(double posXPlanet1, double posYPlanet1, double velXPlanet1, double velYPlanet1, double m1,
                                     double posXPlanet2, double posYPlanet2, double velXPlanet2, double velYPlanet2, double m2) {

        double d = Math.sqrt(Math.pow(posXPlanet2 - posXPlanet1, 2) + Math.pow(posYPlanet2 - posYPlanet1, 2));  // Distance between 2 planets
        double force = G * m1 * m2 / Math.pow(d, 2);

        return new double[]{force * (posXPlanet2 - posXPlanet1) / d, force * (posYPlanet2 - posYPlanet1) / d};  // Return x- and y- Force Component
    }

}
