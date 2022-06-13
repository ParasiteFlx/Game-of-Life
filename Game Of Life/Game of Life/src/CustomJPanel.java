import java.awt.Dimension;

import javax.swing.JPanel;

public class CustomJPanel extends JPanel{
	
	private Dimension getCustomDimensions(){
            return new Dimension((int)(super.getParent().getBounds().width * 0.8), super.getParent().getBounds().height);
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
