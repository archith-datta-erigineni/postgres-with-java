package com.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CarApp extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new CarApp();
        frame.setTitle("Car App");
        frame.setBounds(100, 100, 450, 700);
        frame.setVisible(true);
    }

    JLabel lblMake, lblModel, lblYear, lblColor, lblSerial;
    JTextField txtMake, txtModel, txtYear, txtColor, txtDel, txtSerial, txtEdit;
    JButton btnRetrieve, btnSubmit, btnDelete, btnEdit;

    CarApp() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        java.awt.Font bigFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 20);

        lblMake = new JLabel("Make:");
        lblMake.setFont(bigFont);
        panel.add(lblMake);
        txtMake = new JTextField(10);
        txtMake.setFont(bigFont);
        panel.add(txtMake);

        lblModel = new JLabel("Model:");
        lblModel.setFont(bigFont);
        panel.add(lblModel);
        txtModel = new JTextField(10);
        txtModel.setFont(bigFont);
        panel.add(txtModel);

        lblYear = new JLabel("Year:");
        lblYear.setFont(bigFont);
        panel.add(lblYear);
        txtYear = new JTextField(10);
        txtYear.setFont(bigFont);
        panel.add(txtYear);

        lblColor = new JLabel("Color:");
        lblColor.setFont(bigFont);
        panel.add(lblColor);
        txtColor = new JTextField(10);
        txtColor.setFont(bigFont);
        panel.add(txtColor);

        lblSerial = new JLabel("VIN:");
        lblSerial.setFont(bigFont);
        panel.add(lblSerial);
        txtSerial = new JTextField(10);
        txtSerial.setFont(bigFont);
        panel.add(txtSerial);

        btnSubmit = new JButton("Submit");
        btnSubmit.setFont(bigFont);
        panel.add(btnSubmit);

        btnRetrieve = new JButton("Retrieve Inventory");
        btnRetrieve.setFont(bigFont);
        panel.add(btnRetrieve);

        btnEdit = new JButton("Edit Car Id #: (click once entered)");
        btnEdit.setFont(bigFont);
        panel.add(btnEdit);
        txtEdit = new JTextField(10);
        txtEdit.setFont(bigFont);
        panel.add(txtEdit);

        btnDelete = new JButton("Delete Car Id #: (click once entered)");
        btnDelete.setFont(bigFont);
        panel.add(btnDelete);
        txtDel = new JTextField(10);
        txtDel.setFont(bigFont);
        panel.add(txtDel);

        add(panel);

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (txtEdit.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Enter a Car ID!");
                        return;
                    }
                } catch (Exception xx) {
                    JOptionPane.showMessageDialog(null, "Enter a Car ID!");
                    return;
                }

                int idCheck = Integer.parseInt(txtEdit.getText());

                if (!checkId(idCheck)) {
                    JOptionPane.showMessageDialog(null, "Invalid Car ID");
                    return;
                }

                JDialog editDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel), "Edit Car", true);
                JPanel editPanel = new JPanel();

                java.awt.Font bigFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 20);
                JTextField txtMakeEdit = new JTextField(10);
                txtMakeEdit.setFont(bigFont);
                JTextField txtModelEdit = new JTextField(10);
                txtModelEdit.setFont(bigFont);
                JTextField txtYearEdit = new JTextField(10);
                txtYearEdit.setFont(bigFont);
                JTextField txtColorEdit = new JTextField(10);
                txtColorEdit.setFont(bigFont);
                JTextField txtSerialEdit = new JTextField(10);
                txtSerialEdit.setFont(bigFont);

                JLabel lblMakeEdit = new JLabel("Make:");
                lblMakeEdit.setFont(bigFont);
                editPanel.add(lblMakeEdit);
                editPanel.add(txtMakeEdit);
                JLabel lblModelEdit = new JLabel("Model:");
                lblModelEdit.setFont(bigFont);
                editPanel.add(lblModelEdit);
                editPanel.add(txtModelEdit);
                JLabel lblYearEdit = new JLabel("Year:");
                lblYearEdit.setFont(bigFont);
                editPanel.add(lblYearEdit);
                editPanel.add(txtYearEdit);
                JLabel lblColorEdit = new JLabel("Color:");
                lblColorEdit.setFont(bigFont);
                editPanel.add(lblColorEdit);
                editPanel.add(txtColorEdit);
                JLabel lblSerialEdit = new JLabel("VIN:");
                lblSerialEdit.setFont(bigFont);
                editPanel.add(lblSerialEdit);
                editPanel.add(txtSerialEdit);

                JButton btnSubmitEdit = new JButton("Submit");
                btnSubmitEdit.setFont(bigFont);
                editPanel.add(btnSubmitEdit);

                editDialog.add(editPanel);
                editDialog.pack();
                editDialog.setLocationRelativeTo(panel);

                btnSubmitEdit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String make = txtMakeEdit.getText();
                        String model = txtModelEdit.getText();
                        String yearStr = txtYearEdit.getText();
                        String color = txtColorEdit.getText();
                        String serial = txtSerialEdit.getText();

                        if (yearStr.isEmpty()) {

                            Check test = new Check(make, model, yearStr, serial);

                            if (!test.verifyModelYear()) {
                                JOptionPane.showMessageDialog(null, "Invalid Car (Car Does Not Exist In Database)");
                                return;
                            }
                            if (test.checkSerial()) {
                                JOptionPane.showMessageDialog(null, "Invalid Car (Car Already Exists In Database)");
                                return;
                            }
                            try {
                                String url = "jdbc:postgresql://localhost:5432/testdb";
                                String user = "postgres";
                                String password = "test";
                                Connection conn = DriverManager.getConnection(url, user, password);
                                System.out.println("Connected to server");
                                Statement stmt = conn.createStatement();

                                boolean changed = false;
                                if (!make.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set make='%s' where id=%d;",
                                            make, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!model.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set model='%s' where id=%d;",
                                            model, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!color.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set color='%s' where id=%d;",
                                            color, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!serial.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set serial='%s' where id=%d;",
                                            serial, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!changed) {
                                    JOptionPane.showMessageDialog(null, "Car Not Edited, No Changes Were Made");
                                    stmt.close();
                                    conn.close();
                                    return;
                                }

                                JOptionPane.showMessageDialog(null, "Car Edited");
                                stmt.close();
                                conn.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        Check test = new Check(make, model, yearStr, serial);

                        if (!test.verifyModelYear()) {
                            JOptionPane.showMessageDialog(null, "Invalid Car (Car Does Not Exist In Database)");
                            return;
                        }
                        if (test.checkSerial()) {
                            JOptionPane.showMessageDialog(null, "Invalid Car (Car Already Exists In Database)");
                            return;
                        }

                        try {
                            int year = Integer.parseInt(yearStr);
                            try {
                                String url = "jdbc:postgresql://localhost:5432/testdb";
                                String user = "postgres";
                                String password = "test";
                                Connection conn = DriverManager.getConnection(url, user, password);
                                System.out.println("Connected to server");
                                Statement stmt = conn.createStatement();

                                boolean changed = false;
                                if (!make.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set make='%s' where id=%d;",
                                            make, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!model.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set model='%s' where id=%d;",
                                            model, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!yearStr.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set year='%d' where id=%d;",
                                            year, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!color.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set color='%s' where id=%d;",
                                            color, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!serial.isEmpty()) {
                                    stmt.executeUpdate(String.format("update inventory set serial='%s' where id=%d;",
                                            serial, Integer.parseInt(txtEdit.getText())));
                                    changed = true;
                                }
                                if (!changed) {
                                    JOptionPane.showMessageDialog(null, "Car Not Edited, No Changes Were Made");
                                    stmt.close();
                                    conn.close();
                                    return;
                                }

                                JOptionPane.showMessageDialog(null, "Car Edited");
                                stmt.close();
                                conn.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } catch (NumberFormatException ne) {
                            JOptionPane.showMessageDialog(null, "Car Can Not Be Edited");
                        }
                        editDialog.dispose();
                    }
                });

                editDialog.setVisible(true);
            }
        });

        btnRetrieve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:postgresql://localhost:5432/testdb";
                String user = "postgres";
                String password = "test";

                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    ArrayList<String> db = new ArrayList<String>();
                    System.out.println("Connected to server");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("select * from inventory;");
                    while (rs.next()) {
                        db.add(rs.getString(1) + ".  " + rs.getString(5) + " " + rs.getString(4) + " " + rs.getString(2)
                                + " " + rs.getString(3) + "\n  VIN: " + rs.getString(6));
                    }

                    String data = "";
                    for (String item : db) {
                        data += item + "\n";
                    }

                    JOptionPane.showMessageDialog(null, data);

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String make = txtMake.getText();
                String model = txtModel.getText();
                String yearStr = txtYear.getText();
                String color = txtColor.getText();
                String serial = txtSerial.getText();
                System.out.println(serial);

                Check test = new Check(make, model, yearStr, serial);

                if (!test.verifyModelYear()) {
                    JOptionPane.showMessageDialog(null, "Invalid Car (Car Does Not Exist In Database)");
                    return;
                }
                if (test.checkSerial()) {
                    JOptionPane.showMessageDialog(null, "Invalid Car (Car Already Exists In Database)");
                    return;
                }

                try {
                    int year = Integer.parseInt(yearStr);
                    try {
                        String url = "jdbc:postgresql://localhost:5432/testdb";
                        String user = "postgres";
                        String password = "test";
                        Connection conn = DriverManager.getConnection(url, user, password);
                        System.out.println("Connected to server");
                        Statement stmt = conn.createStatement();
                        int id = getId() + 1;
                        int rs = stmt.executeUpdate(
                                String.format("insert into inventory values('%d', '%s', '%s', '%s', '%s', '%s');", id,
                                        make, model, year, color, serial));
                        JOptionPane.showMessageDialog(null, "Car Added");
                        System.out.println("Added " + rs + " rows");
                        stmt.close();
                        conn.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(null, "Car Can Not Be Added, Invalid Year");
                }

            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idToDelStr = txtDel.getText();

                try {
                    if (txtDel.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Enter a Car ID!");
                        return;
                    }
                } catch (Exception xx) {
                    JOptionPane.showMessageDialog(null, "Enter a Car ID!");
                    return;
                }

                int idToDel = Integer.parseInt(idToDelStr);
                if (!checkId(idToDel)) {
                    JOptionPane.showMessageDialog(null, "Invalid Car ID");
                    return;
                }

                try {
                    String url = "jdbc:postgresql://localhost:5432/testdb";
                    String user = "postgres";
                    String password = "test";
                    Connection conn = DriverManager.getConnection(url, user, password);
                    System.out.println("Connected to server");
                    Statement stmt = conn.createStatement();
                    // delete row of given id car
                    int rs = stmt.executeUpdate(String.format("delete from inventory where id=%d;", idToDel));
                    JOptionPane.showMessageDialog(null, String.format("Car %d Deleted", idToDel));
                    System.out.println("Deleted " + rs + " rows");

                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });

    }

    public int retRows() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String user = "postgres";
        String password = "test";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to server");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery("select count(*) from inventory;");
            resultSet.last();
            int size = resultSet.getRow();
            resultSet.beforeFirst();
            stmt.close();
            conn.close();
            return (size);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getId() {
        String url = "jdbc:postgresql://localhost:5432/testdb";
        String user = "postgres";
        String password = "test";

        try {
            int id = 0;
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to server");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("select * from inventory;");
            while (rs.next()) {
                if (rs.isLast()) {
                    id = rs.getInt(1);
                }
            }
            stmt.close();
            conn.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public boolean checkSerial(String serial) {
        try {
            String url = "jdbc:postgresql://localhost:5432/testdb";
            String user = "postgres";
            String password = "test";
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to server");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(String.format("select count(*) from inventory where serial='%s';", serial));

            System.out.println("Counted " + rs + " rows");

            stmt.close();
            conn.close();
            

            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean checkId(int id) {
        try {
            String url = "jdbc:postgresql://localhost:5432/testdb";
            String user = "postgres";
            String password = "test";
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to server");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(String.format("select count(*) from inventory where id='%d';", id));
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            System.out.println("Counted " + count + " rows");

            rs.close();
            stmt.close();
            conn.close();

            return count > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
