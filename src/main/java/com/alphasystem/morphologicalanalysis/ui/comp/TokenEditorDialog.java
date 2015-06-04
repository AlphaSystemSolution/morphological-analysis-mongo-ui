/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.comp;

import com.alphasystem.arabic.model.ArabicLetter;
import com.alphasystem.arabic.model.ArabicWord;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.repository.LocationRepository;
import com.alphasystem.morphologicalanalysis.repository.TokenRepository;
import com.alphasystem.morphologicalanalysis.ui.MorphologicalAnalysisFrame;
import com.alphasystem.morphologicalanalysis.ui.util.ComboBoxHolder;
import com.alphasystem.morphologicalanalysis.ui.util.LocationAdapter;
import com.alphasystem.morphologicalanalysis.ui.util.TokenAdapter;
import com.alphasystem.morphologicalanalysis.util.MorphologicalAnalysisRepositoryUtil;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;
import com.alphasystem.persistence.mongo.spring.support.ApplicationContextProvider;
import com.alphasystem.ui.AbstractComponentAction;
import com.alphasystem.ui.BaseDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import static com.alphasystem.arabic.ui.util.Constants.ARABIC_FONT_BOLD_20;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.support.PartOfSpeech.NOUN;
import static com.alphasystem.ui.ExceptionViewer.showExceptionViewer;
import static com.alphasystem.ui.util.SpringUtilities.makeCompactGrid;
import static com.jidesoft.dialog.ButtonPanel.OTHER_BUTTON;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.lang.String.format;
import static javax.swing.BorderFactory.*;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.Box.createVerticalStrut;
import static javax.swing.JOptionPane.*;
import static javax.swing.border.EtchedBorder.LOWERED;

/**
 * @author sali
 * 
 */
public class TokenEditorDialog extends BaseDialog {

	private static final long serialVersionUID = -5607347859022932005L;

	private static final int HORIZONTAL_STRUT_WIDTH = 10;

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(format("%s", TokenEditorDialog.class.getName()));

	private static LocationRepository locationRepository;

	private static TokenRepository tokenRepository;

	static {
		ApplicationContextProvider context = ApplicationContextProvider
				.getInstance();
		MorphologicalAnalysisRepositoryUtil repositoryUtil = context
				.getBean(MorphologicalAnalysisRepositoryUtil.class);
		locationRepository = repositoryUtil.getLocationRepository();
		tokenRepository = repositoryUtil.getTokenRepository();
	}

	private JComboBox locationComboBox;
	private Token token;
	private List<Location> locations;
	private ArabicLetter[] letters;
	private JPanel propertiesPanel;
	private TokenWordPreviewPanel tokenWordPreviewPanel;
	private ComboBoxHolder comboBoxHolder;
	private RootWordSelectionPanel rootWordSelectionPanel;
	private JButton saveButton;
	private AbstractAction partOfSpeechAction = new AbstractAction() {

		private static final long serialVersionUID = -2320436473203213310L;

		@Override
		public void actionPerformed(ActionEvent e) {
			partOfSpeechAction();
		}
	};

	public TokenEditorDialog(MorphologicalAnalysisFrame parent, Token token) {
		super(parent, "Token Editor", false);

		comboBoxHolder = ComboBoxHolder.getInstance();
		initDialog(token, false);

		setLocationRelativeTo(parent);
		pack();
		setLocation(20, 20);
	}

	private static String getString(Class<?> klass) {
		return RESOURCE_BUNDLE.getString(format("%s.label",
				klass.getSimpleName()));
	}

	@Override
	public JButton[] getButtons() {
		return new JButton[] { okButton, getSaveButton(), cancelButton };
	}

	private void addSubPropertiesPanel(JPanel mainPanel,
			PartOfSpeech partOfSpeech) {
		Component subPropertiesPanel = getSubPropertiesPanel(partOfSpeech);

		int rows = 1;
		if (subPropertiesPanel != null) {
			mainPanel.add(subPropertiesPanel);
			rows++;
		}

		makeCompactGrid(mainPanel, rows, 1, 0, 0, 0, 0);
	}

	@Override
	public JComponent createContentPanel() {
		JPanel contentPanel = new JPanel(new BorderLayout());

		JPanel innerPanel = new JPanel(new SpringLayout());

		int rows = 0;
		int cols = 1;

		tokenWordPreviewPanel = new TokenWordPreviewPanel(letters);
		innerPanel.add(tokenWordPreviewPanel);
		rows++;
		innerPanel.add(createLocationComboBoxPanel());
		rows++;

		tokenWordPreviewPanel.selectButtons(
				comboBoxHolder.getSelectedLocation(), letters);

		makeCompactGrid(innerPanel, rows, cols);

		contentPanel.add(innerPanel, NORTH);
		propertiesPanel = createPropertiesMainPanel();
		contentPanel.add(propertiesPanel, CENTER);

		setInitFocusedComponent(okButton);
		return contentPanel;
	}

	private JPanel createLocationComboBoxPanel() {
		JPanel panel = new JPanel();

		initLocationComboBox();
		panel.add(locationComboBox);

		LocationAdapter selectedItem = (LocationAdapter) locationComboBox
				.getSelectedItem();
		comboBoxHolder.setSelectedLocation(selectedItem.getLocation());

		return panel;
	}

	private JPanel createNounPropertiesPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel label = null;
		int rows = 0;
		int cols = 3;

		JPanel innerPanel = new JPanel(new SpringLayout());

		label = new JLabel(getString(NounStatus.class));
		innerPanel.add(label);
		label = new JLabel(getString(NumberType.class));
		innerPanel.add(label);
		label = new JLabel(getString(GenderType.class));
		innerPanel.add(label);
		rows++;

		innerPanel.add(comboBoxHolder.getNounStatusComboBox());
		innerPanel.add(comboBoxHolder.getNumberTypeComboBox());
		innerPanel.add(comboBoxHolder.getGenderTypeComboBox());
		rows++;

		label = new JLabel(getString(NounKind.class));
		innerPanel.add(label);
		label = new JLabel(getString(NounType.class));
		innerPanel.add(label);
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		innerPanel.add(comboBoxHolder.getNounKindComboBox());
		innerPanel.add(comboBoxHolder.getNounTypeComboBox());
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		makeCompactGrid(innerPanel, rows, cols);

		mainPanel.add(innerPanel, CENTER);
		mainPanel.setBorder(createCompoundBorder(createEmptyBorder(6, 6, 6, 6),
				createEtchedBorder(LOWERED)));

		return mainPanel;
	}

	private JPanel createProNounPropertiesPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel label = null;
		int rows = 0;
		int cols = 3;

		JPanel innerPanel = new JPanel(new SpringLayout());

		label = new JLabel(getString(ProNounType.class));
		innerPanel.add(label);
		label = new JLabel(getString(NumberType.class));
		innerPanel.add(label);
		label = new JLabel(getString(GenderType.class));
		innerPanel.add(label);
		rows++;

		innerPanel.add(comboBoxHolder.getProNounTypeComboBox());
		innerPanel.add(comboBoxHolder.getNumberTypeComboBox());
		innerPanel.add(comboBoxHolder.getGenderTypeComboBox());
		rows++;

		label = new JLabel(getString(ConversationType.class));
		innerPanel.add(label);
		label = new JLabel(getString(NounStatus.class));
		innerPanel.add(label);
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		innerPanel.add(comboBoxHolder.getConversationTypeComboBox());
		innerPanel.add(comboBoxHolder.getNounStatusComboBox());
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		makeCompactGrid(innerPanel, rows, cols);

		mainPanel.add(innerPanel, CENTER);
		mainPanel.setBorder(createCompoundBorder(createEmptyBorder(6, 6, 6, 6),
				createEtchedBorder(LOWERED)));
		return mainPanel;
	}

	private JPanel createPropertiesMainPanel() {
		JPanel mainPanel = new JPanel(new SpringLayout());

		mainPanel.add(createPropertiesPanel());

		Location selectedLocation = comboBoxHolder.getSelectedLocation();
		PartOfSpeech partOfSpeech = selectedLocation == null ? NOUN
				: selectedLocation.getPartOfSpeech();
		addSubPropertiesPanel(mainPanel, partOfSpeech);

		mainPanel.setBorder(createCompoundBorder(
				createTitledBorder("Properties"),
				createEmptyBorder(12, 12, 12, 12)));

		comboBoxHolder.selectComboBoxes(partOfSpeechAction);
		return mainPanel;
	}

	private JPanel createPropertiesPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());

		JLabel label = null;
		int rows = 0;
		int cols = 2;

		JPanel innerPanel = new JPanel(new SpringLayout());

		label = new JLabel(getString(PartOfSpeech.class));
		innerPanel.add(label);
		label = new JLabel(RESOURCE_BUNDLE.getString("rootWord.label"));
		innerPanel.add(label);
		rows++;

		JComboBox partOfSpeechComboBox = comboBoxHolder
				.getPartOfSpeechComboBox();
		partOfSpeechComboBox.addActionListener(partOfSpeechAction);
		innerPanel.add(partOfSpeechComboBox);

		rootWordSelectionPanel = new RootWordSelectionPanel(comboBoxHolder
				.getSelectedLocation().getRootWord());
		innerPanel.add(rootWordSelectionPanel);
		rows++;

		label = new JLabel(getString(NamedTemplate.class));
		innerPanel.add(label);
		label = new JLabel(getString(NamedTag.class));
		innerPanel.add(label);
		rows++;

		innerPanel.add(comboBoxHolder.getFormTemplateComboBox());
		innerPanel.add(comboBoxHolder.getNamedTagComboBox());
		rows++;

		// label = new JLabel(getString(GrammaticalRelationship.class));
		// innerPanel.add(label);
		// innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		// rows++;

		// grammaticalTermPanel = new GrammaticalTermPanel(
		// comboBoxHolder.getSelectedLocation());
		// innerPanel.add(grammaticalTermPanel);
		// innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		// rows++;

		makeCompactGrid(innerPanel, rows, cols, 0, 0, 20, 10);

		mainPanel.add(innerPanel, CENTER);

		return mainPanel;
	}

	private JPanel createVerbPropertiesPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel label = null;
		int rows = 0;
		int cols = 3;

		JPanel innerPanel = new JPanel(new SpringLayout());

		label = new JLabel(getString(VerbType.class));
		innerPanel.add(label);
		label = new JLabel(getString(ConversationType.class));
		innerPanel.add(label);
		label = new JLabel(getString(VerbMode.class));
		innerPanel.add(label);
		rows++;

		innerPanel.add(comboBoxHolder.getVerbTypeComboBox());
		innerPanel.add(comboBoxHolder.getConversationTypeComboBox());
		innerPanel.add(comboBoxHolder.getVerbModeComboBox());
		rows++;

		label = new JLabel(getString(NumberType.class));
		innerPanel.add(label);
		label = new JLabel(getString(GenderType.class));
		innerPanel.add(label);
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		innerPanel.add(comboBoxHolder.getNumberTypeComboBox());
		innerPanel.add(comboBoxHolder.getGenderTypeComboBox());
		innerPanel.add(createHorizontalStrut(HORIZONTAL_STRUT_WIDTH));
		rows++;

		makeCompactGrid(innerPanel, rows, cols);

		mainPanel.add(innerPanel, CENTER);
		mainPanel.setBorder(createCompoundBorder(createEmptyBorder(6, 6, 6, 6),
				createEtchedBorder(LOWERED)));
		return mainPanel;
	}

	@Override
	protected boolean doOnOK(ActionEvent event) {
		boolean result = true;
		int confirm = showConfirmDialog(this, "Are you sure?", "Question",
				YES_NO_OPTION, QUESTION_MESSAGE);
		if (YES_OPTION == confirm) {
			updateLocation();
		}
		return result;
	}

	@Override
	public String getCancelButtonText() {
		return "Close";
	}

	@Override
	public String getOkButtonText() {
		return "Save & Close";
	}

	protected RootWord getRootWords() {
		RootWord rootWord = comboBoxHolder.getSelectedLocation().getRootWord();
		return rootWord == null ? new RootWord() : rootWord;
	}

	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.putClientProperty(BUTTON_TYPE_KEY, OTHER_BUTTON);
			saveButton.setAction(new AbstractComponentAction("Save") {

				private static final long serialVersionUID = -5890663575784841609L;

				@Override
				public void actionPerformed(ActionEvent e) {
					int confirm = showConfirmDialog(TokenEditorDialog.this,
							"Are you sure?", "Question", YES_NO_OPTION,
							QUESTION_MESSAGE);
					if (YES_OPTION == confirm) {
						updateLocation();
					}
				}
			});
		}
		return saveButton;
	}

	private Component getSubPropertiesPanel(PartOfSpeech partOfSpeech) {
		Component subPropertiesPanel = null;

		AbstractProperties properties = comboBoxHolder.getSelectedLocation()
				.getProperties();
		if (isNoun(properties)) {
			subPropertiesPanel = createNounPropertiesPanel();
		} else if (isPronoun(properties)) {
			subPropertiesPanel = createProNounPropertiesPanel();
		} else if (isVerb(properties)) {
			subPropertiesPanel = createVerbPropertiesPanel();
		} else {
			subPropertiesPanel = createVerticalStrut(20);
		}
		return subPropertiesPanel;
	}

	public void initDialog(Token token) {
		initDialog(token, true);
	}

	private void initDialog(Token token, boolean reInit) {
		comboBoxHolder.setSelectedLocation(null);
		this.token = token;
		locations = token.getLocations();
		ArabicWord src = token.getTokenWord();

		setTitle(format("Token - %s", new TokenAdapter(token)));

		int len = src.getLength();
		letters = new ArabicLetter[len];

		for (int i = len - 1; i >= 0; i--) {
			letters[i] = src.getLetter(i);
		}

		if (reInit) {
			updateLocationComboBox();
			tokenWordPreviewPanel.updateButtons(letters);
			locationComboBoxAction();
		}
	}

	@SuppressWarnings({"unchecked"})
	private void initLocationComboBox() {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();

		for (Location location : locations) {
			model.addElement(new LocationAdapter(location));
		}

		locationComboBox = new JComboBox(model);
		setInitFocusedComponent(locationComboBox);
		locationComboBox.setFont(ARABIC_FONT_BOLD_20);
		locationComboBox.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = -2320436473203213310L;

			@Override
			public void actionPerformed(ActionEvent e) {
				locationComboBoxAction();
			}
		});
	}

	private AbstractProperties loadNounProperties(NounProperties properties) {
		properties.withNounKind(comboBoxHolder.getSelectedNounKind())
				.withNounType(comboBoxHolder.getSelectedNounType())
				.withNounStatus(comboBoxHolder.getSelectedNounStatus())
				.withGenderType(comboBoxHolder.getSelectedGenderType())
				.withNumberType(comboBoxHolder.getSelectedNumberType());
		return properties;
	}

	private AbstractProperties loadProNounProperties(
			ProNounProperties properties) {
		properties
				.withConversationType(
						comboBoxHolder.getSelectedConversationType())
				.withProNounType(comboBoxHolder.getSelectedProNounType())
				.withNounStatus(comboBoxHolder.getSelectedNounStatus())
				.withGenderType(comboBoxHolder.getSelectedGenderType())
				.withNumberType(comboBoxHolder.getSelectedNumberType());
		return properties;
	}

	private AbstractProperties loadVerbProperties(VerbProperties properties) {
		properties
				.withConversationType(
						comboBoxHolder.getSelectedConversationType())
				.withVerbMode(comboBoxHolder.getSelectedVerbMode())
				.withVerbType(comboBoxHolder.getSelectedVerbType())
				.withGenderType(comboBoxHolder.getSelectedGenderType())
				.withNumberType(comboBoxHolder.getSelectedNumberType());
		return properties;
	}

	private void locationComboBoxAction() {
		LocationAdapter selectedItem = (LocationAdapter) locationComboBox
				.getSelectedItem();
		Location selectedLocation = selectedItem.getLocation();
		comboBoxHolder.setSelectedLocation(selectedLocation);
		tokenWordPreviewPanel.selectButtons(selectedLocation, letters);
		updateSubPropertiesPanel(selectedLocation.getPartOfSpeech());
		rootWordSelectionPanel.updatePanel(selectedLocation.getRootWord());
	}

	private void partOfSpeechAction() {
		PartOfSpeech partOfSpeech = comboBoxHolder.updatePartOfSpeech();
		updateSubPropertiesPanel(partOfSpeech);
	}

	private void populateLocationProperties(Location location) {
		AbstractProperties properties = location.getProperties();
		if (isNoun(properties)) {
			properties = loadNounProperties((NounProperties) properties);
		} else if (isPronoun(properties)) {
			properties = loadProNounProperties((ProNounProperties) properties);
		} else if (isVerb(properties)) {
			properties = loadVerbProperties((VerbProperties) properties);
		}
		location.setProperties(properties);
	}

	private void updateLocation() {
		// TODO: add GrammaticalTerms
		Location selectedLocation = comboBoxHolder.getSelectedLocation();
		selectedLocation
				.withPartOfSpeech(comboBoxHolder.getSelectedPartOfSpeech())
				.withNamedTag(comboBoxHolder.getSelectedNamedTag())
				.withRootWord(rootWordSelectionPanel.getRootWord())
				.withForm(comboBoxHolder.getSelectedFormTemplate());
		populateLocationProperties(selectedLocation);
		tokenWordPreviewPanel.updateLocation(selectedLocation);
		try {
			selectedLocation = locationRepository.save(selectedLocation);
			locations = token.getLocations();
			ListIterator<Location> listIterator = locations.listIterator();
			while (listIterator.hasNext()) {
				Location location = listIterator.next();
				if (location.equals(selectedLocation)) {
					listIterator.set(selectedLocation);
					break;
				}
			}
			Location lastLocation = locations.get(token.getLocationCount() - 1);
			if (!lastLocation.isTransient()
					&& lastLocation.getEndIndex() < token.getTokenWord()
							.getLength()) {
				// there are still some unselected, so create new location
				Location location = new Location(token.getChapterNumber(),
						token.getVerseNumber(), token.getTokenNumber(),
						token.getLocationCount() + 1);
				locationRepository.save(location);
				token.addLocation(location);
				token = tokenRepository.save(token);
				locations = token.getLocations();
				updateLocationComboBox();
				locationComboBox.setSelectedItem(location);
			}
		} catch (Exception e) {
			showExceptionViewer("Error", parentFrame, e);
		}
	}

	@SuppressWarnings({"unchecked"})
	private void updateLocationComboBox() {
		locationComboBox.removeAll();
		final DefaultComboBoxModel model = new DefaultComboBoxModel();

		for (Location location : locations) {
			model.addElement(new LocationAdapter(location));
		}

		locationComboBox.setModel(model);
		pack();
	}

	private void updateSubPropertiesPanel(PartOfSpeech partOfSpeech) {
		if (propertiesPanel == null) {
			return;
		}
		if (propertiesPanel.getComponentCount() >= 2) {
			propertiesPanel.remove(1);
		}
		addSubPropertiesPanel(propertiesPanel, partOfSpeech);
		comboBoxHolder.selectComboBoxes(partOfSpeechAction);
		pack();
	}

}
