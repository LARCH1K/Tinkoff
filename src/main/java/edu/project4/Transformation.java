package edu.project4;

import edu.project4.models.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.function.UnaryOperator;

public class Transformation implements UnaryOperator<Point> {
    final Color color;
    final double[] coefficients;
    static Variations usedVariations = new Variations();

    Transformation(Color color, double a, double b, double c, double d, double e, double f) {
        this.color = color;
        coefficients = new double[] {a, b, c, d, e, f};
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public Point apply(Point pointWithColor) {
        final Point2D point2D = pointWithColor.point;
        double resX = 0.0;
        double resY = 0.0;
        double x = point2D.getX();
        double y = point2D.getY();
        final double new_x = coefficients[0] * x + coefficients[1] * y + coefficients[2];
        final double new_y = coefficients[3] * x + coefficients[4] * y + coefficients[5];
        final Point2D new_point = new Point2D.Double(new_x, new_y);
        for (int i = 0; i < usedVariations.coefficients.size(); i++) {
            final double w = usedVariations.coefficients.get(i);
            final Point2D p = Variations.VARIATIONS.get(i).apply(new_point);
            resX += w * p.getX();
            resY += w * p.getY();
        }
        return new Point(
            new Point2D.Double(resX, resY),
            getNewColor(pointWithColor.color)
        );
    }

    private Color getNewColor(Color color) {
        return new Color(
            (this.color.getRed() + color.getRed()) / 2,
            (this.color.getGreen() + color.getGreen()) / 2,
            (this.color.getBlue() + color.getBlue()) / 2
        );
    }
}
