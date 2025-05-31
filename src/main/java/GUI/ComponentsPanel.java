/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Мария
 */





import Service.Database;
import model.WandComponent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ComponentsPanel extends JPanel {
    private final Database dbManager;
    private JTable componentsTable;
    
    public ComponentsPanel(Database dbManager) {
        this.dbManager = dbManager;
        initializeUI();
        loadComponents();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = createStyledButton("Обновить");
        refreshButton.addActionListener(e -> loadComponents());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.NORTH);
        
        componentsTable = new JTable() {
            // Увеличиваем высоту строк
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(super.getPreferredScrollableViewportSize().width, getRowHeight() * 10);
            }
        };
        
        // Устанавливаем высоту строк
        componentsTable.setRowHeight(40); // Увеличиваем высоту строк до 40 пикселей
        
        // Устанавливаем шрифт большего размера для лучшей читаемости
        componentsTable.setFont(new Font("Bahnschrift SemiBold SemiConden", Font.PLAIN, 14));
        
        componentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(componentsTable);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadComponents() {
        try {
            if (dbManager.getAllComponents().isEmpty()) {
                createInitialComponentsWithZeroQuantity();
            }
            List<WandComponent> components = dbManager.getAllComponents();
            
            String[] columnNames = {"Идентификатор", "Тип", "Наименование", "Количество"};
            Object[][] data = new Object[components.size()][4];
            
            for (int i = 0; i < components.size(); i++) {
                WandComponent component = components.get(i);
                data[i][0] = component.getId();
                data[i][1] = component.getType();
                data[i][2] = component.getName();
                data[i][3] = component.getQuantity();
            }
            
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            componentsTable.setModel(model);
            
            // Настраиваем ширину столбцов
            setColumnWidths();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Ошибка загрузки данных: " + e.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void setColumnWidths() {
        // Устанавливаем предпочтительные ширины столбцов
        componentsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
        componentsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Тип
        componentsTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Наименование
        componentsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Количество
        
        // Разрешаем растягивание только последнего столбца
        componentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }
    
    // Остальные методы без изменений
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 190));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
        
        return button;
    }
    
    private void createInitialComponentsWithZeroQuantity() throws SQLException {
        WandComponent[] initialComponents = {
            new WandComponent("wood", "Дуб", 0),
            new WandComponent("wood", "Ясень", 0),
            new WandComponent("core", "Перо феникса", 0),
            new WandComponent("core", "Волос из хвоста единорога", 0)
        };

        for (WandComponent component : initialComponents) {
            dbManager.addComponent(component);
        }
    }
}    