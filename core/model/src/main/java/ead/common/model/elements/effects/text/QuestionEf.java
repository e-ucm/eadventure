/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.common.model.elements.effects.text;

import ead.common.interfaces.Element;
import ead.common.interfaces.Param;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.AbstractEffect;
import ead.common.model.elements.extra.EAdList;
import ead.common.model.params.text.EAdString;

/**
 * <p>
 * Effect ShowQuestion
 * </p>
 * 
 */
@Element
public class QuestionEf extends AbstractEffect {

	@Param
	private EAdString question;

    @Param
    private EAdList<EAdString> answers;

    @Param
    private EAdList<EAdList<EAdEffect>> effects;

	@Param
	private boolean randomAnswers;

	public QuestionEf() {
		super();
		answers = new EAdList<EAdString>();
        effects = new EAdList<EAdList<EAdEffect>>();
	}

	public void addAnswer(EAdString string, EAdList<EAdEffect> effects) {
        answers.add(string);
        this.effects.add(effects);
	}

    public void addAnswer(EAdString string, EAdEffect effect){
        EAdList<EAdEffect> effects = new EAdList<EAdEffect>();
        effects.add(effect);
        this.addAnswer(string, effects);
    }

	public EAdString getQuestion() {
		return question;
	}

	public void setQuestion(EAdString question) {
		this.question = question;
	}

	public boolean isRandomAnswers() {
		return randomAnswers;
	}

	public void setRandomAnswers(boolean randomAnswers) {
		this.randomAnswers = randomAnswers;
	}

    public EAdList<EAdString> getAnswers() {
        return answers;
    }

    public void setAnswers(EAdList<EAdString> answers) {
        this.answers = answers;
    }

    public EAdList<EAdList<EAdEffect>> getEffects() {
        return effects;
    }

    public void setEffects(EAdList<EAdList<EAdEffect>> effects) {
        this.effects = effects;
    }
}
