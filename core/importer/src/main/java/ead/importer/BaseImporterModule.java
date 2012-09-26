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

package ead.importer;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import ead.common.model.elements.EAdAction;
import ead.common.model.elements.EAdAdventureModel;
import ead.common.model.elements.EAdChapter;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.EffectsMacro;
import ead.common.model.elements.effects.text.SpeakEf;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.EAdSceneElement;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.trajectories.NodeTrajectoryDefinition;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.importer.auxiliar.EAdElementFactoryImpl;
import ead.importer.auxiliar.ImporterImageLoaderFactory;
import ead.importer.auxiliar.inputstreamcreators.ImporterInputStreamCreator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.resources.AnimationImporter;
import ead.importer.resources.FrameImporter;
import ead.importer.resources.ResourceImporterImpl;
import ead.importer.subimporters.AdventureImporter;
import ead.importer.subimporters.books.BookImporter;
import ead.importer.subimporters.chapter.ActionImporter;
import ead.importer.subimporters.chapter.AtrezzoImporter;
import ead.importer.subimporters.chapter.ChapterImporter;
import ead.importer.subimporters.chapter.ItemImporter;
import ead.importer.subimporters.chapter.NPCImporter;
import ead.importer.subimporters.chapter.TimerImporter;
import ead.importer.subimporters.chapter.conversations.ConversationImporter;
import ead.importer.subimporters.chapter.conversations.DialogueNodeImporter;
import ead.importer.subimporters.chapter.conversations.LineImporterToCaption;
import ead.importer.subimporters.chapter.conversations.LineImporterToShowText;
import ead.importer.subimporters.chapter.cutscene.SlidesceneImporter;
import ead.importer.subimporters.chapter.cutscene.VideosceneImporter;
import ead.importer.subimporters.chapter.scene.BarrierImporter;
import ead.importer.subimporters.chapter.scene.SceneImporter;
import ead.importer.subimporters.chapter.scene.TrajectoryImporter;
import ead.importer.subimporters.chapter.scene.elements.ActiveAreaImporter;
import ead.importer.subimporters.chapter.scene.elements.ElementReferenceImporter;
import ead.importer.subimporters.chapter.scene.elements.ExitImporter;
import ead.importer.subimporters.conditions.ConditionsImporter;
import ead.importer.subimporters.conditions.FlagConditionImporter;
import ead.importer.subimporters.conditions.VarConditionImporter;
import ead.importer.subimporters.effects.EffectsImporterModule;
import ead.importer.subimporters.macros.MacroImporter;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.GraphConversation;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.effects.Macro;
import es.eucm.eadventure.common.data.chapter.elements.ActiveArea;
import es.eucm.eadventure.common.data.chapter.elements.Atrezzo;
import es.eucm.eadventure.common.data.chapter.elements.Barrier;
import es.eucm.eadventure.common.data.chapter.elements.Item;
import es.eucm.eadventure.common.data.chapter.elements.NPC;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;
import es.eucm.eadventure.common.loader.InputStreamCreator;

/**
 * Guice module to configure the <e-Adventure> 1.X game importer, without
 * specifying an annotator to use
 */
public class BaseImporterModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new EffectsImporterModule());

		bind(new TypeLiteral<EAdElementImporter<AdventureData, EAdAdventureModel>>() {})
                .to(AdventureImporter.class);
		EAdElementFactoryImpl.importerMap.put(AdventureData.class,
				AdventureImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Chapter, EAdChapter>>() {})
				.to(ChapterImporter.class);
		EAdElementFactoryImpl.importerMap.put(Chapter.class,
				ChapterImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Scene, BasicScene>>() {})
				.to(SceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Scene.class, SceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Slidescene, EAdScene>>() {})
				.to(SlidesceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Slidescene.class,
				SlidesceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Videoscene, EAdScene>>() {})
				.to(VideosceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Videoscene.class,
				VideosceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<ElementReference, EAdSceneElement>>() {})
				.to(ElementReferenceImporter.class);
		EAdElementFactoryImpl.importerMap.put(ElementReference.class,
				ElementReferenceImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Atrezzo, EAdSceneElementDef>>() {})
				.to(AtrezzoImporter.class);
		EAdElementFactoryImpl.importerMap.put(Atrezzo.class,
				AtrezzoImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Item, EAdSceneElementDef>>() {})
				.to(ItemImporter.class);
		EAdElementFactoryImpl.importerMap.put(Item.class, ItemImporter.class);

		bind(new TypeLiteral<EAdElementImporter<NPC, EAdSceneElementDef>>() {})
				.to(NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(NPC.class, NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(Player.class, NPCImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conditions, EAdCondition>>() {})
				.to(ConditionsImporter.class);
		EAdElementFactoryImpl.importerMap.put(Conditions.class,
				ConditionsImporter.class);

		EAdElementFactoryImpl.importerMap.put(GlobalState.class,
				ConditionsImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Macro, EffectsMacro>>() {})
				.to(MacroImporter.class);
		EAdElementFactoryImpl.importerMap.put(Macro.class, MacroImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Action, EAdAction>>() {})
				.to(ActionImporter.class);
		EAdElementFactoryImpl.importerMap.put(Action.class,
				ActionImporter.class);

		bind(new TypeLiteral<GenericImporter<Animation, FramesAnimation>>() {})
				.to(AnimationImporter.class);
		EAdElementFactoryImpl.importerMap.put(Animation.class,
				AnimationImporter.class);

		bind(new TypeLiteral<GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame>>() {})
				.to(FrameImporter.class);
		EAdElementFactoryImpl.importerMap.put(
				es.eucm.eadventure.common.data.animation.Frame.class,
				FrameImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Exit, EAdSceneElement>>() {})
				.to(ExitImporter.class);
		EAdElementFactoryImpl.importerMap.put(Exit.class, ExitImporter.class);

		bind(new TypeLiteral<EAdElementImporter<FlagCondition, OperationCond>>() {})
				.to(FlagConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(es.eucm.eadventure.common.data.chapter.conditions.FlagCondition.class,
						FlagConditionImporter.class);

		bind(new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, OperationCond>>() {})
				.to(VarConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(es.eucm.eadventure.common.data.chapter.conditions.VarCondition.class,
						VarConditionImporter.class);

		bind(new TypeLiteral<EAdElementImporter<ActiveArea, EAdSceneElement>>() {})
				.to(ActiveAreaImporter.class);
		EAdElementFactoryImpl.importerMap.put(ActiveArea.class,
				ActiveAreaImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conversation, EAdEffect>>() {})
				.to(ConversationImporter.class);

		EAdElementFactoryImpl.importerMap.put(Conversation.class,
				ConversationImporter.class);
		EAdElementFactoryImpl.importerMap.put(GraphConversation.class,
				ConversationImporter.class);

		bind(new TypeLiteral<EAdElementImporter<DialogueConversationNode, EAdEffect>>() {})
				.to(DialogueNodeImporter.class);
		EAdElementFactoryImpl.importerMap.put(DialogueConversationNode.class,
				DialogueNodeImporter.class);

		bind(new TypeLiteral<GenericImporter<ConversationLine, EAdCaption>>() {})
				.to(LineImporterToCaption.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToCaption.class);

		bind(new TypeLiteral<EAdElementImporter<ConversationLine, SpeakEf>>() {}).to(LineImporterToShowText.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToShowText.class);

		bind(new TypeLiteral<EAdElementImporter<Timer, EAdEvent>>() {})
				.to(TimerImporter.class);
		EAdElementFactoryImpl.importerMap.put(Timer.class, TimerImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Trajectory, NodeTrajectoryDefinition>>() {})
				.to(TrajectoryImporter.class);
		EAdElementFactoryImpl.importerMap.put(Trajectory.class,
				TrajectoryImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Barrier, EAdSceneElement>>() {})
				.to(BarrierImporter.class);
		EAdElementFactoryImpl.importerMap.put(Barrier.class,
				BarrierImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Book, EAdScene>>() {})
				.to(BookImporter.class);
		EAdElementFactoryImpl.importerMap.put(Book.class, BookImporter.class);

		bind(ResourceImporter.class).to(ResourceImporterImpl.class);
		bind(EAdElementFactory.class).to(EAdElementFactoryImpl.class);
		bind(ImageLoaderFactory.class).to(ImporterImageLoaderFactory.class);
		bind(InputStreamCreator.class).to(ImporterInputStreamCreator.class);
	}
}
