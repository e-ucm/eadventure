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

package es.eucm.eadventure.common.impl.importer;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.GenericImporter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.GraphConversation;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.OptionConversationNode;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.impl.importer.auxiliar.EAdElementFactoryImpl;
import es.eucm.eadventure.common.impl.importer.auxiliar.ImporterImageLoaderFactory;
import es.eucm.eadventure.common.impl.importer.auxiliar.TemporalInputStreamCreator;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.impl.importer.resources.AnimationImporter;
import es.eucm.eadventure.common.impl.importer.resources.FrameImporter;
import es.eucm.eadventure.common.impl.importer.resources.ResourceImporterImpl;
import es.eucm.eadventure.common.impl.importer.subimporters.AdventureImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ActionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.AtrezzoImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ChapterImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ItemImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.NPCImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.ConversationImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.DialogueNodeImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.LineImporterToCaption;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.LineImporterToShowText;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.OptionConversationImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.cutscene.SlidesceneImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ActiveAreaImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ElementReferenceImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ExitImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.SceneImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.ConditionsImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.FlagConditionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.VarConditionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectsImporterModule;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.model.EAdAdventureModel;
import es.eucm.eadventure.common.model.EAdChapter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowQuestion;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdCutscene;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;

/**
 * Guice module to configure the <e-Adventure> 1.X game importer
 */
public class ImporterConfigurationModule extends AbstractModule {

	private String projectFolder;

	public ImporterConfigurationModule(String projectFolder) {
		this.projectFolder = projectFolder;
	}

	@Override
	protected void configure() {
		this.install(new EffectsImporterModule());

		bind(String.class).annotatedWith(Names.named("projectFolder"))
				.toInstance(projectFolder);

		bind(new TypeLiteral<EAdElementImporter<AdventureData, EAdAdventureModel>>() {}).to(AdventureImporter.class);
		EAdElementFactoryImpl.importerMap.put(AdventureData.class, AdventureImporter.class);
		
		bind(new TypeLiteral<EAdElementImporter<Chapter, EAdChapter>>() {}).to(ChapterImporter.class);
		EAdElementFactoryImpl.importerMap.put(Chapter.class, ChapterImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Scene, EAdSceneImpl>>() {}).to(SceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Scene.class, SceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Slidescene, EAdCutscene>>() {}).to(SlidesceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Slidescene.class, SlidesceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<ElementReference, EAdActorReference>>() {}).to(ElementReferenceImporter.class);
		EAdElementFactoryImpl.importerMap.put(ElementReference.class, ElementReferenceImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Atrezzo, EAdActor>>() {}).to(AtrezzoImporter.class);
		EAdElementFactoryImpl.importerMap.put(Atrezzo.class, AtrezzoImporter.class);
		
		bind(new TypeLiteral<EAdElementImporter<Item, EAdActor>>() {}).to(ItemImporter.class);
		EAdElementFactoryImpl.importerMap.put(Item.class, ItemImporter.class);

		bind(new TypeLiteral<EAdElementImporter<NPC, EAdActor>>() {}).to(NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(NPC.class, NPCImporter.class);

		
		bind(new TypeLiteral<EAdElementImporter<Conditions, EAdCondition>>() {}).to(ConditionsImporter.class);
		EAdElementFactoryImpl.importerMap.put(Conditions.class, ConditionsImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Action, EAdAction>>() {}).to(ActionImporter.class);
		EAdElementFactoryImpl.importerMap.put(Action.class, ActionImporter.class);

		bind(new TypeLiteral<GenericImporter<Animation, FramesAnimation>>() {}).to(AnimationImporter.class);
		EAdElementFactoryImpl.importerMap.put(Animation.class, AnimationImporter.class);

		bind(
				new TypeLiteral<GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame>>() {
				}).to(FrameImporter.class);
		EAdElementFactoryImpl.importerMap.put(es.eucm.eadventure.common.data.animation.Frame.class, FrameImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Exit, EAdSceneElement>>() {
		}).to(ExitImporter.class);
		EAdElementFactoryImpl.importerMap.put(Exit.class, ExitImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.FlagCondition, FlagCondition>>() {
				}).to(FlagConditionImporter.class);
		EAdElementFactoryImpl.importerMap.put(es.eucm.eadventure.common.data.chapter.conditions.FlagCondition.class, FlagConditionImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition>>() {
				}).to(VarConditionImporter.class);
		EAdElementFactoryImpl.importerMap.put(es.eucm.eadventure.common.data.chapter.conditions.VarCondition.class, VarConditionImporter.class);

		bind(new TypeLiteral<EAdElementImporter<ActiveArea, EAdSceneElement>>() {
		}).to(ActiveAreaImporter.class);
		EAdElementFactoryImpl.importerMap.put(ActiveArea.class, ActiveAreaImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conversation, EAdEffect>>() {
		}).to(ConversationImporter.class);

		EAdElementFactoryImpl.importerMap.put(Conversation.class, ConversationImporter.class);
		EAdElementFactoryImpl.importerMap.put(GraphConversation.class, ConversationImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<DialogueConversationNode, EAdTriggerMacro>>() {
				}).to(DialogueNodeImporter.class);
		EAdElementFactoryImpl.importerMap.put(DialogueConversationNode.class, DialogueNodeImporter.class);

		bind(new TypeLiteral<GenericImporter<ConversationLine, Caption>>() {
		}).to(LineImporterToCaption.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class, LineImporterToCaption.class);

		bind(new TypeLiteral<EAdElementImporter<ConversationLine, EAdShowText>>() {
		}).to(LineImporterToShowText.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class, LineImporterToShowText.class);

		bind(
				new TypeLiteral<EAdElementImporter<OptionConversationNode, EAdShowQuestion>>() {
				}).to(OptionConversationImporter.class);
		EAdElementFactoryImpl.importerMap.put(OptionConversationNode.class, OptionConversationImporter.class);

		bind(ResourceImporter.class).to(ResourceImporterImpl.class);
		bind(EAdElementFactory.class).to(EAdElementFactoryImpl.class);
		bind(InputStreamCreator.class).to(TemporalInputStreamCreator.class);
		bind(ImageLoaderFactory.class).to(ImporterImageLoaderFactory.class);
	}

}
