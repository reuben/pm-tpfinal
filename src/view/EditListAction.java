package view;

import model.TaskType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 *  A simple popup editor for a JList that allows you to change
 *  the value in the selected row.
 *
 *  The default implementation has a few limitations:
 *
 *  a) the JList must be using the DefaultListModel
 *
 *  If you which to use a different model or different data then you must
 *  extend this class and:
 *
 *  a) invoke the setModelClass(...) method to specify the ListModel you need
 *  b) override the applyValueToModel(...) method to update the model
 */
class EditListAction extends AbstractAction
{
    private JList list;
    private final Runnable editCallback;

    private JPopupMenu editPopup;
    private JTextField editTextField;
    private Class<?> modelClass;

    public EditListAction(Runnable callback) {
        this.editCallback = callback;
        setModelClass(DefaultListModel.class);
    }

    protected void setModelClass(Class modelClass)
    {
        this.modelClass = modelClass;
    }

    protected void applyValueToModel(TaskTypeCheckBox value, ListModel model, int row)
    {
        DefaultListModel dlm = (DefaultListModel)model;
        dlm.set(row, value);
    }

    /*
     *  Display the popup editor when requested
     */
    public void actionPerformed(ActionEvent e)
    {
        actionPerformed((JList)e.getSource());
    }

    public void actionPerformed(JList source)
    {
        list = source;
        ListModel model = list.getModel();

        if (! modelClass.isAssignableFrom(model.getClass())) return;

        //  Do a lazy creation of the popup editor
        if (editPopup == null)
            createEditPopup();

        //  Position the popup editor over top of the selected row
        int row = list.getSelectedIndex();
        Rectangle r = list.getCellBounds(row, row);

        editPopup.setPreferredSize(new Dimension(r.width, r.height));
        editPopup.show(list, r.x, r.y);

        //  Prepare the text field for editing
        editTextField.setText("");
        editTextField.selectAll();
        editTextField.requestFocusInWindow();
    }

    /*
     *  Create the popup editor
     */
    private void createEditPopup()
    {
        //  Use a text field as the editor
        editTextField = new JTextField();
        Border border = UIManager.getBorder("List.focusCellHighlightBorder");
        editTextField.setBorder( border );

        //  Add an Action to the text field to save the new value to the model
        editTextField.addActionListener(e -> {
            TaskType taskType = new TaskType(editTextField.getText());
            TaskTypeCheckBox checkBox = new TaskTypeCheckBox(taskType, true);
            ListModel model = list.getModel();
            int row = list.getSelectedIndex();
            applyValueToModel(checkBox, model, row);
            editPopup.setVisible(false);
            editCallback.run();
        });

        //  Add the editor to the popup
        editPopup = new JPopupMenu();
        editPopup.setBorder( new EmptyBorder(0, 0, 0, 0) );
        editPopup.add(editTextField);
    }
}
