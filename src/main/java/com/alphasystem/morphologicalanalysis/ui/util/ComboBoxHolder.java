/**
 * 
 */
package com.alphasystem.morphologicalanalysis.ui.util;

import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.wordbyword.model.*;
import com.alphasystem.morphologicalanalysis.wordbyword.model.support.*;

import javax.swing.*;
import java.awt.event.ActionListener;

import static com.alphasystem.arabic.ui.util.ComboBoxHelper.*;
import static com.alphasystem.morphologicalanalysis.wordbyword.model.AbstractProperties.*;

/**
 * @author sali
 * 
 */
public class ComboBoxHolder {

	private static ComboBoxHolder instance;
	private final JComboBox partOfSpeechComboBox;
	private final JComboBox namedTagComboBox;
	private final JComboBox grammaticalTermComboBox;
	private final JComboBox nounStatusComboBox;
	private final JComboBox numberTypeComboBox;
	private final JComboBox genderTypeComboBox;
	private final JComboBox nounKindComboBox;
	private final JComboBox nounTypeComboBox;
	private final JComboBox conversationTypeComboBox;
	private final JComboBox proNounTypeComboBox;
	private final JComboBox verbTypeComboBox;
	private final JComboBox verbModeComboBox;
	private final JComboBox formTemplateComboBox;
	private Location selectedLocation;

	/**
	 * do not let anyone instantiate this class.
	 */
	private ComboBoxHolder() {
		partOfSpeechComboBox = getComboBox(getPartOfSpeechModel());
		namedTagComboBox = getComboBox(getNamedTagModel());
		grammaticalTermComboBox = getComboBox(getGrammaticalTermModel());
		nounStatusComboBox = getComboBox(getNounStatusModel());
		numberTypeComboBox = getComboBox(getNumberTypeModel());
		genderTypeComboBox = getComboBox(getGenderTypeModel());
		nounKindComboBox = getComboBox(getNounKindModel());
		nounTypeComboBox = getComboBox(getNounTypeModel());
		conversationTypeComboBox = getComboBox(getConversationTypeModel());
		proNounTypeComboBox = getComboBox(getProNounTypeModel());
		verbTypeComboBox = getComboBox(getVerbTypeModel());
		verbModeComboBox = getComboBox(getVerbModeModel());
		formTemplateComboBox = getComboBox(getFormTemplateModel());
	}

	/**
	 * @return
	 */
	public synchronized static ComboBoxHolder getInstance() {
		if (instance == null) {
			instance = new ComboBoxHolder();
		}
		return instance;
	}

	public JComboBox getConversationTypeComboBox() {
		return conversationTypeComboBox;
	}

	private DefaultComboBoxModel getConversationTypeModel() {
		return getComboBoxModel(ConversationType.values(), true);
	}

	public JComboBox getFormTemplateComboBox() {
		return formTemplateComboBox;
	}

	private DefaultComboBoxModel getFormTemplateModel() {
		return getComboBoxModel(NamedTemplate.values(), true);
	}

	public JComboBox getGenderTypeComboBox() {
		return genderTypeComboBox;
	}

	private DefaultComboBoxModel getGenderTypeModel() {
		return getComboBoxModel(GenderType.values(), true);
	}

	public JComboBox getGrammaticalTermComboBox() {
		return grammaticalTermComboBox;
	}

	private DefaultComboBoxModel getGrammaticalTermModel() {
		return getComboBoxModel(RelationshipType.values(), true);
	}

	public JComboBox getNamedTagComboBox() {
		return namedTagComboBox;
	}

	private DefaultComboBoxModel getNamedTagModel() {
		return getComboBoxModel(NamedTag.values(), true);
	}

	public JComboBox getNounKindComboBox() {
		return nounKindComboBox;
	}

	private DefaultComboBoxModel getNounKindModel() {
		return getComboBoxModel(NounKind.values(), true);
	}

	public JComboBox getNounStatusComboBox() {
		return nounStatusComboBox;
	}

	private DefaultComboBoxModel getNounStatusModel() {
		return getComboBoxModel(NounStatus.values(), true);
	}

	public JComboBox getNounTypeComboBox() {
		return nounTypeComboBox;
	}

	private DefaultComboBoxModel getNounTypeModel() {
		return getComboBoxModel(NounType.values(), true);
	}

	public JComboBox getNumberTypeComboBox() {
		return numberTypeComboBox;
	}

	private DefaultComboBoxModel getNumberTypeModel() {
		return getComboBoxModel(NumberType.values(), true);
	}

	public JComboBox getPartOfSpeechComboBox() {
		return partOfSpeechComboBox;
	}

	private DefaultComboBoxModel getPartOfSpeechModel() {
		return getComboBoxModel(PartOfSpeech.values(), false);
	}

	public JComboBox getProNounTypeComboBox() {
		return proNounTypeComboBox;
	}

	private DefaultComboBoxModel getProNounTypeModel() {
		return getComboBoxModel(ProNounType.values(), true);
	}

	private AbstractProperties getProperties() {
		return selectedLocation == null ? null : selectedLocation
				.getProperties();
	}

	public ConversationType getSelectedConversationType() {
		return getSelectedValue(ConversationType.class,
				conversationTypeComboBox);
	}

	public NamedTemplate getSelectedFormTemplate() {
		return getSelectedValue(NamedTemplate.class, formTemplateComboBox);
	}

	public GenderType getSelectedGenderType() {
		return getSelectedValue(GenderType.class, genderTypeComboBox);
	}

	public RelationshipType getSelectedGrammaticalTerm() {
		return getSelectedValue(RelationshipType.class, grammaticalTermComboBox);
	}

	public Location getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(Location selectedLocation) {
		this.selectedLocation = selectedLocation;
	}

	public NamedTag getSelectedNamedTag() {
		return getSelectedValue(NamedTag.class, namedTagComboBox);
	}

	public NounKind getSelectedNounKind() {
		return getSelectedValue(NounKind.class, nounKindComboBox);
	}

	public NounStatus getSelectedNounStatus() {
		return getSelectedValue(NounStatus.class, nounStatusComboBox);
	}

	public NounType getSelectedNounType() {
		return getSelectedValue(NounType.class, nounTypeComboBox);
	}

	public NumberType getSelectedNumberType() {
		return getSelectedValue(NumberType.class, numberTypeComboBox);
	}

	public PartOfSpeech getSelectedPartOfSpeech() {
		return getSelectedValue(PartOfSpeech.class, partOfSpeechComboBox);
	}

	public ProNounType getSelectedProNounType() {
		return getSelectedValue(ProNounType.class, proNounTypeComboBox);
	}

	public VerbMode getSelectedVerbMode() {
		return getSelectedValue(VerbMode.class, verbModeComboBox);
	}

	public VerbType getSelectedVerbType() {
		return getSelectedValue(VerbType.class, verbTypeComboBox);
	}

	public JComboBox getVerbModeComboBox() {
		return verbModeComboBox;
	}

	private DefaultComboBoxModel getVerbModeModel() {
		return getComboBoxModel(VerbMode.values(), true);
	}

	public JComboBox getVerbTypeComboBox() {
		return verbTypeComboBox;
	}

	private DefaultComboBoxModel getVerbTypeModel() {
		return getComboBoxModel(VerbType.values(), true);
	}

	public void selectComboBoxes(ActionListener partOfSpeechAction) {
		selectPartOfSpeech(partOfSpeechAction);
		selectGrammaticalTerm();
		selectNamedTag();
		selectNounStatusComboBox();
		selectNumberTypeComboBox();
		selectGenderTypeComboBox();
		selectNounKindComboBox();
		selectNounTypeComboBox();
		selectConverstationTypeComboBox();
		selectProNounTypeComboBox();
		selectVerbTypeComboBox();
		selectVerbModeComboBox();
		selectFormTemplateComboBox();
	}

	public void selectConverstationTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		ConversationType converstationType = null;
		if (isPronoun(properties)) {
			ProNounProperties pnp = (ProNounProperties) properties;
			converstationType = pnp.getConversationType();
		} else if (isVerb(properties)) {
			VerbProperties vp = (VerbProperties) properties;
			converstationType = vp.getConversationType();
		}
		index = (converstationType == null ? -1 : converstationType.ordinal()) + 1;
		conversationTypeComboBox.setSelectedIndex(index);
	}

	private void selectFormTemplateComboBox() {
		int index = 0;
		NamedTemplate selectedFt = null;
		if (selectedLocation != null) {
			selectedFt = selectedLocation.getFormTemplate();
			index = (selectedFt == null ? -1 : selectedFt.ordinal()) + 1;
		}
		formTemplateComboBox.setSelectedIndex(index);
	}

	public void selectGenderTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (properties != null) {
			GenderType genderType = properties.getGender();
			index = (genderType == null ? -1 : genderType.ordinal()) + 1;
		}
		genderTypeComboBox.setSelectedIndex(index);
	}

	/**
	 * TODO:
	 */
	public void selectGrammaticalTerm() {
		// int index = 0;
		// RelationshipType selectedGt = null;
		// if (selectedLocation != null) {
		// selectedGt = selectedLocation.getGrammaticalTerm();
		// index = (selectedGt == null ? -1 : selectedGt.ordinal()) + 1;
		// }
		// grammaticalTermComboBox.setSelectedIndex(index);
	}

	public void selectNamedTag() {
		int index = 0;
		NamedTag selectedNt = null;
		if (selectedLocation != null) {
			selectedNt = selectedLocation.getNamedTag();
			index = (selectedNt == null ? -1 : selectedNt.ordinal()) + 1;
		}
		namedTagComboBox.setSelectedIndex(index);
	}

	public void selectNounKindComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isNoun(properties)) {
			NounProperties np = (NounProperties) properties;
			NounKind nounKind = np.getNounKind();
			index = (nounKind == null ? -1 : nounKind.ordinal()) + 1;
		}
		nounKindComboBox.setSelectedIndex(index);
	}

	public void selectNounStatusComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isNoun(properties) || isPronoun(properties)) {
			AbstractNounProperties np = (AbstractNounProperties) properties;
			NounStatus status = np.getStatus();
			index = (status == null ? -1 : status.ordinal()) + 1;
		}
		nounStatusComboBox.setSelectedIndex(index);
	}

	public void selectNounTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isNoun(properties)) {
			NounProperties np = (NounProperties) properties;
			NounType nounType = np.getNounType();
			index = (nounType == null ? -1 : nounType.ordinal()) + 1;
		}
		nounTypeComboBox.setSelectedIndex(index);
	}

	public void selectNumberTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (properties != null) {
			NumberType number = properties.getNumber();
			index = (number == null ? -1 : number.ordinal()) + 1;
		}
		numberTypeComboBox.setSelectedIndex(index);
	}

	public void selectPartOfSpeech(ActionListener partOfSpeechAction) {
		int index = 0;
		PartOfSpeech selectedPos = null;
		if (selectedLocation != null) {
			selectedPos = selectedLocation.getPartOfSpeech();
			index = selectedPos == null ? 0 : selectedPos.ordinal();
		}
		partOfSpeechComboBox.removeActionListener(partOfSpeechAction);
		partOfSpeechComboBox.setSelectedIndex(index);
		partOfSpeechComboBox.addActionListener(partOfSpeechAction);
	}

	public void selectProNounTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isPronoun(properties)) {
			ProNounProperties pnp = (ProNounProperties) properties;
			ProNounType proNounType = pnp.getProNounType();
			index = (proNounType == null ? -1 : proNounType.ordinal()) + 1;
		}
		proNounTypeComboBox.setSelectedIndex(index);
	}

	public void selectVerbModeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isVerb(properties)) {
			VerbProperties vp = (VerbProperties) properties;
			VerbMode mode = vp.getMode();
			index = (mode == null ? -1 : mode.ordinal()) + 1;
		}
		verbModeComboBox.setSelectedIndex(index);
	}

	public void selectVerbTypeComboBox() {
		int index = 0;
		AbstractProperties properties = getProperties();
		if (isVerb(properties)) {
			VerbProperties vp = (VerbProperties) properties;
			VerbType verbType = vp.getVerbType();
			index = (verbType == null ? -1 : verbType.ordinal()) + 1;
		}
		verbTypeComboBox.setSelectedIndex(index);
	}

	public PartOfSpeech updatePartOfSpeech() {
		PartOfSpeech partOfSpeech = getSelectedPartOfSpeech();
		if (selectedLocation != null) {
			selectedLocation.setPartOfSpeech(partOfSpeech);
		}
		return partOfSpeech;
	}

}
