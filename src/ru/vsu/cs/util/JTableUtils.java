package ru.vsu.cs.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;


/**
 * Набор функций для работы с JTable (ввода и отбражения массивов)
 * @author Дмитрий Соломатин (кафедра ПиИТ ФКН ВГУ)
 *
 * @see <a href="http://java-online.ru/swing-jtable.xhtml">http://java-online.ru/swing-jtable.xhtml</a>
 */
public class JTableUtils {

    public static class JTableUtilsException extends ParseException {
        public JTableUtilsException(String s) {
            super(s, 0);
        }
    }


    public static final int DEFAULT_GAP = 6;
    public static final int DEFAULT_PLUSMINUS_BUTTONS_SIZE = 22;
    public static final int DEFAULT_COLUMN_WIDTH = 40;
    public static final int DEFAULT_ROW_HEADER_WIDTH = 40;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final char DELETE_KEY_CHAR_CODE = 127;
    private static final Border DEFAULT_CELL_BORDER = BorderFactory.createEmptyBorder(0, 3, 0, 3);
    private static final Border DEFAULT_RENDERER_CELL_BORDER = DEFAULT_CELL_BORDER;
    private static final Border DEFAULT_EDITOR_CELL_BORDER = BorderFactory.createEmptyBorder(0, 3, 0, 2);

    private static final Map<JTable, Integer> tableColumnWidths = new HashMap<>();

    private static final NumberFormat defaultNumberFormat = NumberFormat.getInstance(Locale.ROOT);
    private static double parseDouble(String s) throws NumberFormatException {
        try {
            return defaultNumberFormat.parse(s).doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }


    private static <T extends JComponent> T setFixedSize(T comp, int width, int height) {
        Dimension d = new Dimension(width, height);
        comp.setMaximumSize(d);
        comp.setMinimumSize(d);
        comp.setPreferredSize(d);
        comp.setSize(d);
        return comp;
    }

    private static JButton createPlusMinusButton(String text, int size) {
        JButton button = new JButton(text);
        setFixedSize(button, size, size).setMargin(new Insets(0, 0, 0, 0));
        button.setFocusable(false);
        button.setFocusPainted(false);
        return button;
    }

    private static int getColumnWidth(JTable table) {
        Integer columnWidth = tableColumnWidths.get(table);
        if (columnWidth == null) {
            if (table.getColumnCount() > 0) {
                columnWidth = table.getWidth() / table.getColumnCount();
            } else {
                columnWidth = DEFAULT_COLUMN_WIDTH;
            }
        }
        return columnWidth;
    }

    private static void recalcJTableSize(JTable table) {
        int width = getColumnWidth(table) * table.getColumnCount();
        int height = 0, rowCount = table.getRowCount();
        for (int r = 0; r < rowCount; r++)
            height += table.getRowHeight(height);
        setFixedSize(table, width, height);

        if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            if (scrollPane.getRowHeader() != null) {
                Component rowHeaderView = scrollPane.getRowHeader().getView();
                if (rowHeaderView instanceof JList) {
                    ((JList) rowHeaderView).setFixedCellHeight(table.getRowHeight());
                }
                scrollPane.getRowHeader().repaint();
            }
        }
    }

    private static void addRowHeader(JTable table, TableModel tableModel, JScrollPane scrollPane) {
        final class RowHeaderRenderer extends JLabel implements ListCellRenderer {
            RowHeaderRenderer() {
                JTableHeader header = table.getTableHeader();
                setOpaque(true);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                setHorizontalAlignment(CENTER);
                setForeground(header.getForeground());
                setBackground(header.getBackground());
                setFont(header.getFont());
            }

            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                setText(String.format("[%d]", index));
                return this;
            }
        }

        ListModel lm = new AbstractListModel() {
            @Override
            public int getSize() {
                return tableModel.getRowCount();
            }

            @Override
            public Object getElementAt(int index) {
                return String.format("[%d]", index);
            }
        };

        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(DEFAULT_ROW_HEADER_WIDTH);
        rowHeader.setFixedCellHeight(
            table.getRowHeight()// + table.getRowMargin()// + table.getIntercellSpacing().height
        );
        rowHeader.setCellRenderer(new RowHeaderRenderer());

        scrollPane.setRowHeaderView(rowHeader);
        scrollPane.getRowHeader().getView().setBackground(scrollPane.getColumnHeader().getBackground());
    }

    /**
     * Настройка JTable для работы с массивами
     * @param table компонент JTable
     * @param defaultColWidth ширина столбцов (ячеек)
     * @param showRowsIndexes показывать индексы строк
     * @param showColsIndexes показывать индексы столбцов
     * @param changeRowsCountButtons добавить кнопки для добавления/удаления строк
     * @param changeColsCountButtons добавить кнопки для добавления/удаления столбцов
     * @param changeButtonsSize размер кнопок для изменения количества строк и столбцов
     * @param changeButtonsMargin отступ кнопок от таблицы (а также расстояние между кнопками)
     */
    public static void initJTableForArray(
        JTable table, int defaultColWidth,
        boolean showRowsIndexes, boolean showColsIndexes,
        boolean changeRowsCountButtons, boolean changeColsCountButtons,
        int changeButtonsSize, int changeButtonsMargin
    ) {
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        if (!showColsIndexes && table.getTableHeader() != null) {
            table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        }
        table.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFillsViewportHeight(false);
        table.setDragEnabled(false);
        //table.setCursor(Cursor.getDefaultCursor());
        table.putClientProperty("terminateEditOnFocusLost", true);

        DefaultTableModel tableModel = new DefaultTableModel(new String[] { "[0]" }, 1) {
            @Override
            public String getColumnName(int index) {
                return String.format("[%d]", index);
            }
        };
        table.setModel(tableModel);
        tableColumnWidths.put(table, defaultColWidth);
        recalcJTableSize(table);

        if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            if (changeRowsCountButtons || changeColsCountButtons) {
                List<Component> linkedComponents = new ArrayList<>();

                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

                BorderLayout borderLayout = new BorderLayout(changeButtonsMargin, changeButtonsMargin);
                FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);

                JPanel panel = new JPanel(borderLayout);
                panel.setBackground(TRANSPARENT);

                if (changeColsCountButtons) {
                    JPanel topPanel = new JPanel(flowLayout);
                    topPanel.setBackground(TRANSPARENT);
                    if (changeRowsCountButtons) {
                        topPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsSize + changeButtonsMargin, changeButtonsSize));
                    }
                    JButton minusButton = createPlusMinusButton("\u2013", changeButtonsSize);
                    minusButton.setName(table.getName() + "-minusColumnButton");
                    minusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.setColumnCount(tableModel.getColumnCount() > 0 ? tableModel.getColumnCount() - 1 : 0);
                        recalcJTableSize(table);
                    });
                    topPanel.add(minusButton);
                    linkedComponents.add(minusButton);
                    topPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsMargin, changeButtonsSize));
                    JButton plusButton = createPlusMinusButton("+", changeButtonsSize);
                    plusButton.setName(table.getName() + "-plusColumnButton");
                    plusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.addColumn(String.format("[%d]", tableModel.getColumnCount()));
                        recalcJTableSize(table);
                    });
                    topPanel.add(plusButton);
                    linkedComponents.add(plusButton);

                    panel.add(topPanel, BorderLayout.NORTH);
                }
                if (changeRowsCountButtons) {
                    JPanel leftPanel = setFixedSize(new JPanel(flowLayout), changeButtonsSize, changeButtonsSize);
                    leftPanel.setBackground(TRANSPARENT);
                    JButton minusButton = createPlusMinusButton("\u2013", changeButtonsSize);
                    minusButton.setName(table.getName() + "-minusRowButton");
                    minusButton.addActionListener((ActionEvent evt) -> {
                        if (tableModel.getRowCount() > 0) {
                            tableModel.removeRow(tableModel.getRowCount() - 1);
                            recalcJTableSize(table);
                        }
                    });
                    leftPanel.add(minusButton);
                    linkedComponents.add(minusButton);
                    leftPanel.add(setFixedSize(new Box.Filler(null, null, null), changeButtonsSize, changeButtonsMargin));
                    JButton plusButton = createPlusMinusButton("+", changeButtonsSize);
                    plusButton.setName(table.getName() + "-plusRowButton");
                    plusButton.addActionListener((ActionEvent evt) -> {
                        tableModel.setRowCount(tableModel.getRowCount() + 1);
                        recalcJTableSize(table);
                    });
                    leftPanel.add(plusButton);
                    linkedComponents.add(plusButton);

                    panel.add(leftPanel, BorderLayout.WEST);
                }
                table.setPreferredScrollableViewportSize(null);
                JScrollPane newScrollPane = new JScrollPane(table);
                newScrollPane.setBackground(scrollPane.getBackground());
                newScrollPane.setBorder(scrollPane.getBorder());
                scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                panel.add(newScrollPane, BorderLayout.CENTER);

                scrollPane.getViewport().removeAll();
                scrollPane.add(panel);
                scrollPane.getViewport().add(panel);

                // привязываем обработчик событий, который активирует и дективирует зависимые
                // компоненты (кнопки) в зависимости от состояния table
                table.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                    if ("enabled".equals(evt.getPropertyName())) {
                        boolean enabled = (boolean) evt.getNewValue();
                        linkedComponents.forEach((comp) -> comp.setEnabled(enabled));
                        if (!enabled) {
                            table.clearSelection();
                        }
                    }
                });
                linkedComponents.forEach((comp) -> comp.setEnabled(table.isEnabled()));

                // иначе определенные проблемы с прозрачностью panel возникают
                scrollPane.setVisible(false);
                scrollPane.setVisible(true);

                scrollPane = newScrollPane;
            }

            // привязываем отбработчик событий, который снимает выделение,
            // а также обработчик событий, который будет изменять размер таблицы при изменении высоты строки
            table.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if ("enabled".equals(evt.getPropertyName())) {
                    boolean enabled = (boolean) evt.getNewValue();
                    if (!enabled) {
                        table.clearSelection();
                    }
                }
                else if ("rowHeight".equals(evt.getPropertyName())) {
                    recalcJTableSize(table);
                }
            });

            // привязываем обработчик событий, который очищает выделенные ячейки по клавише delete
            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    if (evt.getKeyChar() == DELETE_KEY_CHAR_CODE) {
                        for (int r : table.getSelectedRows()) {
                            for (int c : table.getSelectedColumns()) {
                                table.setValueAt(null, r, c);
                            }
                        }
                    }
                }
            });

            // устанавливаем CellRenderer, который меняет выравнивание в ячейках в зависимости
            // от содержимого (целые числа - выравнивание вправо, иначе - влево) + красивые отступы
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (comp instanceof JLabel) {
                        JLabel label = (JLabel) comp;
                        label.setHorizontalAlignment((value == null || value.toString().matches("|-?\\d+")) ? RIGHT : LEFT);
                        label.setBorder(DEFAULT_RENDERER_CELL_BORDER);
                    }
                    return comp;
                }
            });
            // устанавливаем CellEditor, который меняет выравнивание в ячейках в зависимости
            // от содержимого (целые числа - выравнивание вправо, иначе - влево) + красивые отступы
            table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                    if (comp instanceof JTextField) {
                        JTextField textField = (JTextField) comp;
                        textField.setHorizontalAlignment((value == null || value.toString().matches("|-?\\d+")) ? SwingConstants.RIGHT : SwingConstants.LEFT);
                        textField.setBorder(DEFAULT_EDITOR_CELL_BORDER);
                        textField.selectAll();  // чтобы при начале печати перезаписывать текст
                    }
                    return comp;
                }
            });

            if (showRowsIndexes) {
                addRowHeader(table, tableModel, scrollPane);
            }
        }
    }

    /**
     * Аналогичен {@link #initJTableForArray(javax.swing.JTable, int, boolean, boolean, boolean, boolean, int, int) }.
     * {@code changeButtonsSize} принимает значение {@link #DEFAULT_PLUSMINUS_BUTTONS_SIZE}.
     * {@code changeButtonsMargin} принимает значение {@link #DEFAULT_GAP}.
     *
     * @see #initJTableForArray(javax.swing.JTable, int, boolean, boolean, boolean, boolean, int, int)
     */
    public static void initJTableForArray(
        JTable table, int defaultColWidth,
        boolean showRowsIndexes, boolean showColsIndexes,
        boolean changeRowsCountButtons, boolean changeColsCountButtons
    ) {
        initJTableForArray(
            table, defaultColWidth,
            showRowsIndexes, showColsIndexes, changeRowsCountButtons, changeColsCountButtons,
            22, DEFAULT_GAP
        );
    }

    /**
     * Изменение размеров Jtable
     * @param table компонент JTable
     * @param rowCount новое кол-во строк (меньше или равно 0 - не менять)
     * @param colCount новое кол-во столбцов (меньше или равно 0 - не менять)
     * @param rowHeight высота строки (меньше или равно 0 - не менять)
     * @param columnWidth ширина столбца (меньше или равно 0 - не менять)
     */
    public static void resizeJTable(JTable table, int rowCount, int colCount, int rowHeight, int columnWidth) {
        if (!(table.getModel() instanceof DefaultTableModel)) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableColumnWidths.put(table, (columnWidth > 0) ? columnWidth : getColumnWidth(table));
        table.setRowHeight((rowHeight > 0) ? rowHeight : table.getRowHeight());
        tableModel.setRowCount((rowCount > 0) ? rowCount : table.getRowCount());
        tableModel.setColumnCount((colCount > 0) ? colCount : table.getColumnCount());
        recalcJTableSize(table);
    }

    /**
     * Изменение размеров Jtable (ширина столбцов и высота строк не меняется)
     * @param table компонент JTable
     * @param rowCount новое кол-во строк
     * @param colCount новое кол-во столбцов
     */
    public static void resizeJTable(JTable table, int rowCount, int colCount) {
        resizeJTable(table, rowCount, colCount, -1, -1);
    }

    /**
     * Изменение размеров ячеек Jtable
     * @param table компонент JTable
     * @param rowHeight высота строки
     * @param columnWidth ширина столбца
     */
    public static void resizeJTableCells(JTable table, int rowHeight, int columnWidth) {
        resizeJTable(table, -1, -1, rowHeight, columnWidth);
    }

    /**
     * Изменение ширины заголовков столбцов
     * @param width Ширина
     */
    public static void setRowsHeaderColumnWidth(JTable table, int width) {
        if (table.getParent() instanceof JViewport && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            if (scrollPane.getRowHeader() != null) {
                Component rowHeaderView = scrollPane.getRowHeader().getView();
                if (rowHeaderView instanceof JList) {
                    ((JList) rowHeaderView).setFixedCellWidth(width);
                }
                scrollPane.getRowHeader().repaint();
            }
        }
    }

    /**
     * Запись данных из массива (одномерного или двухмерного) в JTable
     * (основная реализация, закрытый метод, используется в остальных writeArrayToJTable)
     */
    private static void writeArrayToJTable(JTable table, Object array, String itemFormat) {
        if (!array.getClass().isArray()) {
            return;
        }
        if (!(table.getModel() instanceof DefaultTableModel)) {
            return;
        }
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        tableColumnWidths.put(table, getColumnWidth(table));

        if (itemFormat == null || itemFormat.length() == 0) {
            itemFormat = "%s";
        }
        int rank = 1;
        int len1 = Array.getLength(array), len2 = -1;
        if (len1 > 0) {
            for (int i = 0; i < len1; i++) {
                Object item = Array.get(array, i);
                if (item != null && item.getClass().isArray()) {
                    rank = 2;
                    len2 = Math.max(Array.getLength(item), len2);
                }
            }
        }
        tableModel.setRowCount(rank == 1 ? 1 : len1);
        tableModel.setColumnCount(rank == 1 ? len1 : len2);
        for (int i = 0; i < len1; i++) {
            if (rank == 1) {
                tableModel.setValueAt(String.format(itemFormat, Array.get(array, i)), 0, i);
            } else {
                Object line = Array.get(array, i);
                if (line != null) {
                    if (line.getClass().isArray()) {
                        int lineLen = Array.getLength(line);
                        for (int j = 0; j < lineLen; j++) {
                            tableModel.setValueAt(String.format(itemFormat, Array.get(line, j)), i, j);
                        }
                    } else {
                        tableModel.setValueAt(String.format(itemFormat, Array.get(array, i)), 0, i);
                    }
                }
            }
        }
        recalcJTableSize(table);
    }

    /**
     * Запись данных из массива int[] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, int[] array) {
        writeArrayToJTable(table, array, "%d");
    }

    /**
     * Запись данных из массива int[][] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, int[][] array) {
        writeArrayToJTable(table, array, "%d");
    }

    /**
     * Запись данных из массива double[] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, double[] array, String itemFormat) {
        writeArrayToJTable(table, (Object) array, itemFormat);
    }

    /**
     * Запись данных из массива double[] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, double[] array) {
        writeArrayToJTable(table, array, "%f");
    }

    /**
     * Запись данных из массива double[][] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, double[][] array, String itemFormat) {
        writeArrayToJTable(table, (Object) array, itemFormat);
    }

    /**
     * Запись данных из массива double[] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, double[][] array) {
        writeArrayToJTable(table, array, "%f");
    }

    /**
     * Запись данных из массива String[] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, String[] array) {
        writeArrayToJTable(table, array, "%s");
    }

    /**
     * Запись данных из массива String[][] в JTable
     * (основная реализация, закрытый метод, используется в ArrayToGrid и Array2ToGrid)
     */
    public static void writeArrayToJTable(JTable table, String[][] array) {
        writeArrayToJTable(table, array, "%s");
    }

    /**
     * Чтение данных из JTable в двухмерный массив
     * (основная реализация, используется в остальных readArrayFromJTable и readMatrixFromJTable)
     */
    public static <T> T[][] readMatrixFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter,
        boolean errorIfEmptyCell, T emptyCellValue
    ) throws JTableUtilsException {
        TableModel tableModel = table.getModel();
        int rowCount = tableModel.getRowCount(), colCount = tableModel.getColumnCount();
        T[][] matrix = (T[][]) Array.newInstance(clazz, rowCount, colCount);
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                T value = null;
                Object obj = tableModel.getValueAt(r, c);
                if (obj == null || obj instanceof String && ((String) obj).trim().length() == 0) {
                    if (errorIfEmptyCell) {
                        throw new JTableUtilsException(String.format("Empty value on (%d, %d) cell", r, c));
                    } else {
                        value = emptyCellValue;
                    }
                } else {
                    value = converter.apply(obj.toString());
                }
                matrix[r][c] = value;
            }
        }
        return matrix;
    }

    /**
     * Чтение данных из JTable в двухмерный массив
     */
    public static <T> T[][] readMatrixFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter, T emptyCellValue
    ) {
        try {
            return readMatrixFromJTable(table, clazz, converter, false, emptyCellValue);
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в двухмерный массив
     */
    public static <T> T[][] readMatrixFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter
    ) throws JTableUtilsException {
        return readMatrixFromJTable(table, clazz, converter, true, null);
    }

    /**
     * Чтение данных из JTable в одномерный массив
     */
    public static <T> T[] readArrayFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter,
        boolean errorIfEmptyCell, T emptyCellValue
    ) throws JTableUtilsException {
        T[][] matrix = readMatrixFromJTable(table, clazz, converter, errorIfEmptyCell, emptyCellValue);
        return matrix[0];
    }

    /**
     * Чтение данных из JTable в одномерный массив
     */
    public static <T> T[] readArrayFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter, T emptyCellValue
    ) {
        try {
            return readArrayFromJTable(table, clazz, converter, false, emptyCellValue);
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в одномерный массив
     */
    public static <T> T[] readArrayFromJTable(
        JTable table, Class<T> clazz, Function<String, ? extends T> converter
    ) throws JTableUtilsException {
        return readArrayFromJTable(table, clazz, converter, true, null);
    }

    /**
     * Чтение данных из JTable в двухмерный массив Integer[][]
     */
    public static int[][] readIntMatrixFromJTable(JTable table) throws ParseException {
        try {
            Integer[][] matrix = readMatrixFromJTable(table, Integer.class, Integer::parseInt, false, 0);
            return (int[][]) Arrays.stream(matrix).map(ArrayUtils::toPrimitive).toArray((n) -> new int[n][]);
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в одномерный массив String[]
     */
    public static int[] readIntArrayFromJTable(JTable table) throws ParseException {
        try {
            return ArrayUtils.toPrimitive(readArrayFromJTable(table, Integer.class, Integer::parseInt, false, 0));
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в двухмерный массив Integer[][]
     */
    public static double[][] readDoubleMatrixFromJTable(JTable table) throws ParseException {
        try {
            Double[][] matrix = readMatrixFromJTable(table, Double.class, JTableUtils::parseDouble, false, 0.0);
            return (double[][]) Arrays.stream(matrix).map(ArrayUtils::toPrimitive).toArray((n) -> new double[n][]);
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в одномерный массив Integer[]
     */
    public static double[] readDoubleArrayFromJTable(JTable table) throws ParseException {
        try {
            return ArrayUtils.toPrimitive(readArrayFromJTable(table, Double.class, JTableUtils::parseDouble, false, 0.0));
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в двухмерный массив String[][]
     */
    public static String[][] readStringMatrixFromJTable(JTable table) {
        try {
            return readMatrixFromJTable(table, String.class, (s) -> s, false, "");
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }

    /**
     * Чтение данных из JTable в одномерный массив String[]
     */
    public static String[] readStringArrayFromJTable(JTable table) {
        try {
            return readArrayFromJTable(table, String.class, (s) -> s, false, "");
        } catch (JTableUtilsException impossible) {
        }
        return null;
    }
}
