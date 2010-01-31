package de.onyxbits.giftedmotion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
 * Edit the framesequence
 */
public class SequenceEditor extends JInternalFrame implements ActionListener,
FrameSequenceListener, ChangeListener, ListSelectionListener {

  /**
   * Dispose codes in readable form
   */
  private String[] dcodes = {
    Dict.get("sequenceeditor.dcodes.0"),
    Dict.get("sequenceeditor.dcodes.1"),
    Dict.get("sequenceeditor.dcodes.2"),
    Dict.get("sequenceeditor.dcodes.3")
  };
  
  /**
   * Lists all frames in the sequence
   */
  private JList frlst;
  
  /**
   * X Offset
   */
  private JSpinner xoff = new JSpinner(new SpinnerNumberModel(0,-1000000,1000000,1));
  
  /**
   * Y Offset
   */
  private JSpinner yoff = new JSpinner(new SpinnerNumberModel(0,-1000000,1000000,1));
  
  /**
   * Peer for SingleFrame.showtime
   */
  private JSpinner showtime = new JSpinner(new SpinnerNumberModel(100,1,1000000,10));
  
  /**
   * Peer for SingleFrame.dispose
   */
  private JSpinner dispose = new JSpinner(new SpinnerListModel(dcodes));
  
  /**
   * Move frame in sequence
   */
  private JButton sooner = new JButton(IO.createIcon("Tango/22x22/actions/go-up.png",Dict.get("sequenceeditor.sooner")));
  
  /**
   * Mode frame in sequence
   */
  private JButton later = new JButton(IO.createIcon("Tango/22x22/actions/go-down.png",Dict.get("sequenceeditor.later")));

  /**
   * Duplicate current frame
   */
  private JButton duplicate = new JButton(IO.createIcon("Tango/22x22/actions/edit-copy.png",Dict.get("sequenceeditor.copy")));
  
  /**
   * Trash current frame
   */
  private JButton delete = new JButton(IO.createIcon("Tango/22x22/actions/edit-delete.png",Dict.get("sequenceeditor.delete")));
  
  /**
   * The checkbox to apply the changes to all frames
   */
  private JCheckBox apply = new JCheckBox(Dict.get("sequenceeditor.apply"),false);
  
  /**
   * The framesequence, displayed
   */
  private FrameSequence seq;
  
  public SequenceEditor(FrameSequence seq) {
    super(Dict.get("sequenceeditor.sequenceeditor.title"),false,false,false,false);
    
    this.seq=seq;
    frlst = new JList(seq.frames);
    
    setContentPane(getContent());
    pack();
    
    sooner.addActionListener(this);
    later.addActionListener(this);
    duplicate.addActionListener(this);
    delete.addActionListener(this);
    dispose.addChangeListener(this);
    frlst.addListSelectionListener(this);
    showtime.addChangeListener(this);
    xoff.addChangeListener(this);
    yoff.addChangeListener(this);
    
    apply.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.apply"));
    sooner.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.sooner"));
    later.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.later"));
    duplicate.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.duplicate"));
    delete.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.delete"));
    dispose.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.dispose"));
    showtime.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.showtime"));
    xoff.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.xoff"));
    yoff.setToolTipText(Dict.get("sequenceeditor.sequenceeditor.yoff"));
    
    dataChanged(seq);
  }

  /**
   * Build the contentpane
   */
  private JPanel getContent() {
    
    JPanel order =new JPanel();
    order.setLayout(new BoxLayout(order,BoxLayout.Y_AXIS));
    order.setBorder(BorderFactory.createTitledBorder(Dict.get("sequenceeditor.getcontent.order")));
    JPanel buttons = new JPanel();
    buttons.add(sooner);
    buttons.add(later);
    buttons.add(Box.createHorizontalGlue());
    buttons.add(duplicate);
    buttons.add(delete);
    
    order.add(new JScrollPane(frlst));
    order.add(buttons);
    
/*
    JPanel settings = new JPanel();
    settings.setBorder(BorderFactory.createTitledBorder(Dict.get("sequenceeditor.getcontent.settings")));
    settings.setLayout(new GridLayout(5,0));
    settings.add(new JLabel(Dict.get("sequenceeditor.getcontent.showtime")));
    settings.add(showtime);
    settings.add(new JLabel(Dict.get("sequenceeditor.getcontent.dispose")));
    settings.add(dispose);
    settings.add(new JLabel(Dict.get("sequenceeditor.getcontent.xoff")));
    settings.add(xoff);
    settings.add(new JLabel(Dict.get("sequenceeditor.getcontent.yoff")));
    settings.add(yoff);
    settings.add(apply);
*/


    JLabel label0 = new JLabel(Dict.get("sequenceeditor.getcontent.showtime"));
    JLabel label1 = new JLabel(Dict.get("sequenceeditor.getcontent.dispose"));
    JLabel label2 = new JLabel(Dict.get("sequenceeditor.getcontent.xoff")); 
    JLabel label3 = new JLabel(Dict.get("sequenceeditor.getcontent.yoff"));
    
    JPanel settings = new JPanel();
    GridBagLayout gbl = new GridBagLayout();
    settings.setLayout(gbl);
    settings.setBorder(BorderFactory.createTitledBorder(Dict.get("sequenceeditor.getcontent.settings")));
    GridBagConstraints gbc = new GridBagConstraints();


    // Component: label0
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,10);
    gbl.setConstraints(label0,gbc);
    settings.add(label0);

    // Component: showtime
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,1);
    gbl.setConstraints(showtime,gbc);
    settings.add(showtime);

    // Component: label1
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,10);
    gbl.setConstraints(label1,gbc);
    settings.add(label1);

    // Component: dispose
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,1);
    gbl.setConstraints(dispose,gbc);
    settings.add(dispose);

    // Component: label2
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,10);
    gbl.setConstraints(label2,gbc);
    settings.add(label2);

    // Component: xoff
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,1);
    gbl.setConstraints(xoff,gbc);
    settings.add(xoff);

    // Component: label3
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,10);
    gbl.setConstraints(label3,gbc);
    settings.add(label3);

    // Component: yoff
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 0.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(0,1,1,1);
    gbl.setConstraints(yoff,gbc);
    settings.add(yoff);

    // Component: apply
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.weightx = 100.0;
    gbc.weighty = 0.0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.ipadx = 0;
    gbc.ipady = 0;
    gbc.insets = new Insets(12,1,1,1);
    gbl.setConstraints(apply,gbc);
    settings.add(apply);



    
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
    content.add(order);
    content.add(settings);
    return content;
  }
  
  public void actionPerformed(ActionEvent e) {
    if (seq.selected==null) return;
    Object src = e.getSource();
    
    if (src==dispose) {
      seq.selected.dispose=0;
      for (int i=0;i<dcodes.length;i++) {
        if (dcodes[i].equals(dispose.getValue())) seq.selected.dispose=i;
      }
      seq.fireDataChanged();
    }
    
    if (src==sooner) seq.move(seq.selected,true);
    if (src==later) seq.move(seq.selected,false);
    if (src==duplicate) {
      seq.add(new SingleFrame(seq.selected), seq.getSelectedIndex());
    }
    if (src==delete) {
      seq.remove(seq.selected);
    }
  }
  
  public void stateChanged(ChangeEvent e) {
    if (seq.selected==null) return;
    
    Object src = e.getSource();
    if (src==showtime) {
      int val=((Integer)showtime.getValue()).intValue();
      if (apply.isSelected()) {
        for (int i=0;i<seq.frames.length;i++) seq.frames[i].showtime=val;
      }
      else seq.selected.showtime=((Integer)showtime.getValue()).intValue();
      seq.fireDataChanged();
    }
    
    if (src==dispose) {
      int val=0;
      for (int i=0;i<dcodes.length;i++) {
        if (dcodes[i].equals(dispose.getValue())) val=i;
      }
      if (apply.isSelected()) {
        for (int i=0;i<seq.frames.length;i++) seq.frames[i].dispose=val;
      }
      else seq.selected.dispose=val;
      seq.fireDataChanged();
    }
    
    if (src==xoff || src==yoff) {
      int x=((Integer)xoff.getValue()).intValue();
      int y=((Integer)yoff.getValue()).intValue();
      Point val = new Point(x,y);
      if (apply.isSelected()) {
        for (int i=0;i<seq.frames.length;i++) seq.frames[i].position=val;
      }
      else seq.selected.position=val;
      seq.fireDataChanged();
    }
  }
  
  public void dataChanged(FrameSequence src) {
    frlst.removeListSelectionListener(this);
    dispose.removeChangeListener(this);
    showtime.removeChangeListener(this);
    xoff.removeChangeListener(this);
    yoff.removeChangeListener(this);
    
    frlst.setListData(seq.frames);
    frlst.setSelectedValue(src.selected,true);
    if (src.selected!=null) {
      dispose.setValue(dcodes[src.selected.dispose]);
      showtime.setValue(new Integer(src.selected.showtime));
      xoff.setValue(new Integer(src.selected.position.x));
      yoff.setValue(new Integer(src.selected.position.y));
    }
    frlst.addListSelectionListener(this);
    dispose.addChangeListener(this);
    showtime.addChangeListener(this);
    xoff.addChangeListener(this);
    yoff.addChangeListener(this);
  }
  
  

  public void valueChanged(ListSelectionEvent e) {
    seq.selected=(SingleFrame)frlst.getSelectedValue();
    seq.fireDataChanged();
  }
  
  
}
