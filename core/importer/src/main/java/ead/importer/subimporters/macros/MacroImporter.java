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

package ead.importer.subimporters.macros;

import com.google.inject.Inject;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.effects.EffectsMacro;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.Macro;

public class MacroImporter implements EAdElementImporter<Macro, EffectsMacro> {

	private EffectsImporterFactory effectImporter;

	protected ImportAnnotator annotator;;

	@Inject
	public MacroImporter(EffectsImporterFactory effectImporter,
			ImportAnnotator annotator) {
		this.effectImporter = effectImporter;
		this.annotator = annotator;
	}

	@Override
	public EffectsMacro init(Macro oldMacro) {
		EffectsMacro macro = new EffectsMacro();
		return macro;
	}

	@Override
	public EffectsMacro convert(Macro oldMacro, Object object) {
		EffectsMacro newMacro = (EffectsMacro) object;

		for (AbstractEffect e : oldMacro.getEffects()) {
			EAdEffect newEffect = (EAdEffect) effectImporter.getEffect(e);
			newMacro.getEffects().add(newEffect);
		}

		return newMacro;
	}
}
