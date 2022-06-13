import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class SquareJFrame extends JFrame{
	
	private Dimension getCustomDimensions(){
        return new Dimension((int)(this.getHeight()*1.2), this.getHeight());
}

@Override
public Dimension getMaximumSize(){
    return getCustomDimensions();
}

@Override
public Dimension getMinimumSize(){
    return getCustomDimensions();
}

@Override
public Dimension getPreferredSize(){
    return getCustomDimensions();
}


}
