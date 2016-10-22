/*
 * SnapShopGUI for the ShopShop class TCSS 305 - Assignment 3: SnapShop
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;

import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * SnapShop GUI for the SnapShop Program that applies filters to selected images.
 * 
 * @author Louis Yang (jeho1994)
 * @version 1.0
 */
public class SnapShopGUI extends JFrame {
    
    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = 5756891741199164658L;
    /** Image that is chosen. */
    private PixelImage myImage;
    /** Filter buttons. */    
    private final List<Filter> myFilterButtons;
    /** All the buttons in the GUI. */
    private final List<JButton> myButtons;
    /** Image file chooser. */
    private final JFileChooser myFileChooser;
    
    /**
     * Constructor to initialize the fields for SnapShopGUI.
     */
    public SnapShopGUI() {
        // JFrame title
        super("TCSS 305 SnapShop");
        myFilterButtons = new ArrayList<Filter>();
        myButtons = new ArrayList<JButton>();
        // file chooser chooses the current directory
        myFileChooser = new JFileChooser(".");
    }
    
    /**
     * Displays the SnapShop GUI.
     */
    public void start() {
        // the close operation of the overall GUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        // attempt to center the frame on the screen
        setLocationRelativeTo(null);
        // left panel of the Frame
        final JPanel leftPanel = new JPanel(new GridLayout(myFilterButtons.size(), 1));
        // adds the filter buttons to the left panel
        addFilterButtons(leftPanel);
        // the south panel of the frame
        final JPanel southPanel = new JPanel();
        // the center panel of the frame
        final JPanel centerPanel = new JPanel(new BorderLayout());
        // the picture that will be used for the GUI and program
        final JLabel lable = new JLabel();
        // creates the open button in the south panel
        createOpenButton(southPanel, centerPanel, lable);
        // creates the save as button in the south panel
        createSaveButton(southPanel);
        // creates the close button in the south panel
        createCloseButton(southPanel, centerPanel, lable);
        // centers the picture in the middle
        lable.setHorizontalAlignment(SwingConstants.CENTER);
        lable.setVerticalAlignment(SwingConstants.CENTER);
        // adds each panel to the frame
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        pack();
        setMinimumSize(getPreferredSize());
        setVisible(true);
        
    }
    
    /**
     * Adding filter buttons.
     * 
     * @param theLeftPanel the left panel of the overall frame.
     */    
    private void addFilterButtons(final JPanel theLeftPanel) {
        /* add all the different filters if an addition filter is added in the format below, 
         * it is still compatible with the overall program
         */
        myFilterButtons.add(new EdgeDetectFilter());
        myFilterButtons.add(new EdgeHighlightFilter());
        myFilterButtons.add(new FlipHorizontalFilter());
        myFilterButtons.add(new FlipVerticalFilter());
        myFilterButtons.add(new GrayscaleFilter());
        myFilterButtons.add(new SharpenFilter());
        myFilterButtons.add(new SoftenFilter());
        for (int i = 0; i < myFilterButtons.size(); i++) {
            theLeftPanel.add(createFilterButton(myFilterButtons.get(i)));
        }
    }
    
    /**
     * Creating initial filter buttons and their action listeners.
     * 
     * @param theFilter the filters of the filter button collection.
     * @return the filter buttons each created with action listeners.
     */  
    private JButton createFilterButton(final Filter theFilter) {
        final JButton button = new JButton(theFilter.getDescription());
        myButtons.add(button);
        button.setEnabled(false);
        // Anonymous class for the filter button listener with no name and no constructor!
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                // applies the filter and repaints the GUI
                theFilter.filter(myImage);
                repaint();
            }
        });
        return button; 
    }
    
    /**
     * Creating the open button with its action listeners.
     * 
     * @param theSouthPanel the south panel of the frame.
     * @param theCenterPanel the center panel of the frame.
     * @param theLabel the label of the picture in the frame.
     */
    private void createOpenButton(final JPanel theSouthPanel, final JPanel theCenterPanel,
                                  final JLabel theLabel) {
        final JButton button = new JButton("Open...");
        theSouthPanel.add(button);
        // Anonymous class for the open button listener with no name and no constructor!
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) { 
                final int result = myFileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    final File fileName = myFileChooser.getSelectedFile();
                    try {
                        myImage = PixelImage.load(fileName);
                    } catch (final IOException e) {
                        JOptionPane.showMessageDialog(null, 
                                             "The selected file did not contain an image!", 
                                             "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                    theLabel.setIcon(new ImageIcon(myImage));
                    theCenterPanel.add(theLabel);
                    setMinimumSize(getPreferredSize());
                    pack();
                    enableButtons();
                }
            }
        });
    }
    
    /**
     * Sets all the buttons to be enabled.
     */
    private void enableButtons() {
        for (int i = 0; i < myButtons.size(); i++) {
            myButtons.get(i).setEnabled(true);
        }
    }
    
    /**
     * Sets all the buttons to be disabled.
     */
    private void disableButtons() {
        for (int i = 0; i < myButtons.size(); i++) {
            myButtons.get(i).setEnabled(false);
        }
    }
    
    /**
     * Creating the save button with its action listeners.
     * 
     * @param theSouthPanel the south panel of the frame.
     */
    private void createSaveButton(final JPanel theSouthPanel) {
        final JButton button = new JButton("Save As...");
        theSouthPanel.add(button).setEnabled(false);
        // add the save button to the collection of buttons for the GUI
        myButtons.add(button);
        // Anonymous class for the save button listener with no name and no constructor!
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) { 
                final int result = myFileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    final File fileName = myFileChooser.getSelectedFile();
                    // call on a helper method to determine its action
                    saveButtonAction(fileName);
                }
            }
        });
    }
    
    /**
     * Runs the save button action where if the filename is already exists or if the file 
     * cannot be saved error is produced, otherwise the picture is saved.
     * 
     * @param theFileName the file name of the chosen save file name.
     */
    // helper method for the createSaveButton inner class ActionListener action performed 
    // method
    private void saveButtonAction(final File theFileName) {
        // EXTRA CREDIT!!
        // if the selected file name already exists, an error is shown and the file is
        // not saved
        if (theFileName.exists()) {
            JOptionPane.showMessageDialog(
                                          null, 
                                          "Select a new filename", 
                                          "Oops", JOptionPane.ERROR_MESSAGE);
        // selected file name does not exist and file is saved if nothing is wrong with
        // the saving procedure
        } else {
            try {
                myImage.save(theFileName);
            } catch (final IOException e) {
                JOptionPane.showMessageDialog(
                                          null, 
                                          "Image could not be saved", 
                                          "Error!!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Creating the close button with its action listeners.
     * 
     * @param theSouthPanel the south panel of the frame.
     * @param theCenterPanel the center panel of the frame.
     * @param theLabel the label or the picture in the frame.
     */
    private void createCloseButton(final JPanel theSouthPanel, final JPanel theCenterPanel,
                                   final JLabel theLabel) {
        final JButton button = new JButton("Close Image");
        theSouthPanel.add(button).setEnabled(false);
        myButtons.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                // the image is removed, Frame goes back to its original size and all the
                // buttons other than open is all disabled
                theCenterPanel.remove(theLabel);
                setMinimumSize(getPreferredSize());
                pack();
                repaint();
                disableButtons();
            }
        });
    }
}