package frontend.view.utils;

import java.awt.*;

public class SquareLayoutManager implements LayoutManager {
    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Not required
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // Not required
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        // Calculate preferred size based on the first (and only) component
        if (parent.getComponentCount() == 0) {
            return new Dimension(0, 0); // No components, so return zero size
        }

        // Get the preferred size of the child component
        Component child = parent.getComponent(0);
        Dimension childPreferredSize = child.getPreferredSize();

        // Make the size square by taking the largest dimension
        int size = Math.max(childPreferredSize.width, childPreferredSize.height);
        return new Dimension(size, size); // Return a square dimension
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return parent.getMinimumSize();
    }

    @Override
    public void layoutContainer(Container parent) {
        if (parent.getComponentCount() == 0) {
            return; // No components to layout
        }

        // Only layout the first component (BoardPanel)
        Component child = parent.getComponent(0);

        // Get parent's dimensions
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();

        // Determine the size for the square (minimum of width and height)
        int size = Math.min(parentWidth, parentHeight);

        // Calculate x and y to center the square
        int x = (parentWidth - size) / 2;
        int y = (parentHeight - size) / 2;

        // Set bounds for the child
        child.setBounds(x, y, size, size);
    }
}
