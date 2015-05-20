/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp;

import com.alphasystem.morphologicalanalysis.model.Token;
import com.alphasystem.morphologicalanalysis.model.Verse;
import com.alphasystem.morphologicalanalysis.ui.MorphologicalAnalysisFrame;
import com.alphasystem.morphologicalanalysis.ui.comp.action.ExitAction;
import com.alphasystem.morphologicalanalysis.ui.comp.action.ExportTokenAction;
import com.alphasystem.morphologicalanalysis.ui.util.TokenViewTableCellRendrer;
import com.alphasystem.morphologicalanalysis.ui.util.TokenViewTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_PLAIN_28;
import static com.alphasystem.morphologicalanalysis.ui.util.TokenViewTableModel.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.event.KeyEvent.VK_F;
import static javax.swing.BorderFactory.*;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * @author sali
 * 
 */
public class MorphologicalAnalysisContentPanel extends JPanel {

	private static final long serialVersionUID = 3381958734647655483L;

	private ChapterVerseSelectionPanel chapterVerseSelectionPanel;

	private JPanel verseViewPanel;

	private JTable table;

	private TokenEditorDialog dialog;

	private MorphologicalAnalysisFrame ownerFrame;

	public MorphologicalAnalysisContentPanel(
			final MorphologicalAnalysisFrame ownerFrame) {
		super(new BorderLayout());

		this.ownerFrame = ownerFrame;
		this.ownerFrame.setJMenuBar(createMenuBar());
		verseViewPanel = new JPanel(new BorderLayout());

		table = new JTable() {

			private static final long serialVersionUID = -6721184797694770720L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				TableCellRenderer tableCellRenderer = new TokenViewTableCellRendrer();
				switch (column) {
				case CHECK_BOX_COLUMN_INDEX:
					tableCellRenderer = super.getCellRenderer(row, column);
					break;
				default:
					break;
				}
				return tableCellRenderer;
			}

		};
		table.setSelectionMode(SINGLE_SELECTION);
		table.setTableHeader(null);
		table.setRowHeight(48);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Token token = getSelectedToken();
					if (token != null) {
						showEditTokenDialog(token);
					}
				}
			}

		});

		chapterVerseSelectionPanel = new ChapterVerseSelectionPanel(this);
		add(chapterVerseSelectionPanel, NORTH);
		updateVerseViewPanel(chapterVerseSelectionPanel.getSelectedVerse());
	}

	public void exit() {
		ownerFrame.exit();
	}

	public List<Token> exportTokens() {
		TokenViewTableModel model = (TokenViewTableModel) table.getModel();
		List<Token> selectedTokens = model.getSelectedTokens();
		if (selectedTokens != null && !selectedTokens.isEmpty()) {
			for (Token token : selectedTokens) {
				System.out.println(token);
			}
		}
		table.updateUI();
		return selectedTokens;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("File");
		menu.setMnemonic(VK_F);

		menu.add(new ExportTokenAction(this));

		menu.addSeparator();

		menu.add(new ExitAction(this));

		menuBar.add(menu);
		return menuBar;
	}

	public MorphologicalAnalysisFrame getOwnerFrame() {
		return ownerFrame;
	}

	private Token getSelectedToken() {
		Token token = null;
		int selectedRowIndex = table.getSelectedRow();
		if (selectedRowIndex >= 0) {
			TokenViewTableModel model = (TokenViewTableModel) table.getModel();
			token = model.getSelectedToken(selectedRowIndex);
		}
		return token;
	}

	private void showEditTokenDialog(Token token) {
		if (dialog == null) {
			dialog = new TokenEditorDialog(ownerFrame, token);
		} else {
			dialog.initDialog(token);
		}
		dialog.setVisible(true);
	}

	public void updateVerseViewPanel(Verse selectedVerse) {
		verseViewPanel.removeAll();

		if (selectedVerse != null) {
			JLabel label = new JLabel(selectedVerse.getVerse().toUnicode());
			label.setFont(ARABIC_FONT_PLAIN_28);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			verseViewPanel.add(label, NORTH);

			table.removeAll();
			TokenViewTableModel dataModel = new TokenViewTableModel(
					selectedVerse);
			table.setModel(dataModel);
			TableColumn tableColumn = null;
			for (int i = 0; i < dataModel.getColumnCount(); i++) {
				tableColumn = table.getColumnModel().getColumn(i);
				if (i == TOKEN_COLUMN_INDEX) {
					tableColumn.setMaxWidth(2500);
					tableColumn.setPreferredWidth(250);
				} else if (i == TOKEN_NUMBER_COLUMN_INDEX) {
					tableColumn.setMaxWidth(2500);
					tableColumn.setPreferredWidth(100);
				} else if (i == CHECK_BOX_COLUMN_INDEX) {
					tableColumn.setMaxWidth(2500);
					tableColumn.setPreferredWidth(50);
				}
			}

			JScrollPane scrollPane = new JScrollPane(table);
			verseViewPanel.add(scrollPane, CENTER);
			verseViewPanel.setBorder(createCompoundBorder(
					createEmptyBorder(6, 6, 6, 6), createEtchedBorder()));
		}

		add(verseViewPanel, CENTER);
		updateUI();
	}
}
