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
import ead.importer.auxiliar.EAdElementFactoryImpl;
import ead.importer.auxiliar.ImporterImageLoaderFactory;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.resources.AnimationImporter;
import ead.importer.resources.FrameImporter;
import ead.importer.resources.ResourceImporterImpl;
import ead.importer.subimporters.AdventureImporter;
import ead.importer.subimporters.books.BookImporter;
import ead.importer.subimporters.chapter.*;
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
import es.eucm.ead.importer.inputstreamcreators.ImporterInputStreamCreator;
import es.eucm.ead.model.assets.drawable.basics.EAdCaption;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.elements.*;
import es.eucm.ead.model.elements.conditions.OperationCond;
import es.eucm.ead.model.elements.effects.text.SpeakEf;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.elements.trajectories.NodeTrajectory;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.chapter.*;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.conditions.FlagCondition;
import es.eucm.eadventure.common.data.chapter.conditions.GlobalState;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.GraphConversation;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.DialogueConversationNode;
import es.eucm.eadventure.common.data.chapter.elements.*;
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

		bind(
				new TypeLiteral<EAdElementImporter<AdventureData, AdventureGame>>() {
				}).to(AdventureImporter.class);
		EAdElementFactoryImpl.importerMap.put(AdventureData.class,
				AdventureImporter.class);

		/*bind(new TypeLiteral<EAdElementImporter<Chapter, Chapter>>() {
		}).to(ChapterImporter.class);
		EAdElementFactoryImpl.importerMap.put(Chapter.class,
				ChapterImporter.class);*/

		bind(new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.scenes.Scene, Scene>>() {
		}).to(SceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(es.eucm.eadventure.common.data.chapter.scenes.Scene.class, SceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Slidescene, Scene>>() {
		}).to(SlidesceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Slidescene.class,
				SlidesceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Videoscene, Scene>>() {
		}).to(VideosceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Videoscene.class,
				VideosceneImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<ElementReference, SceneElement>>() {
				}).to(ElementReferenceImporter.class);
		EAdElementFactoryImpl.importerMap.put(ElementReference.class,
				ElementReferenceImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<Atrezzo, SceneElementDef>>() {
				}).to(AtrezzoImporter.class);
		EAdElementFactoryImpl.importerMap.put(Atrezzo.class,
				AtrezzoImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Item, SceneElementDef>>() {
		}).to(ItemImporter.class);
		EAdElementFactoryImpl.importerMap.put(Item.class, ItemImporter.class);

		bind(new TypeLiteral<EAdElementImporter<NPC, SceneElementDef>>() {
		}).to(NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(NPC.class, NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(Player.class, NPCImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conditions, Condition>>() {
		}).to(ConditionsImporter.class);
		EAdElementFactoryImpl.importerMap.put(Conditions.class,
				ConditionsImporter.class);

		EAdElementFactoryImpl.importerMap.put(GlobalState.class,
				ConditionsImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Action, SceneElementDef>>() {
		}).to(ActionImporter.class);
		EAdElementFactoryImpl.importerMap.put(Action.class,
				ActionImporter.class);

		bind(new TypeLiteral<GenericImporter<Animation, FramesAnimation>>() {
		}).to(AnimationImporter.class);
		EAdElementFactoryImpl.importerMap.put(Animation.class,
				AnimationImporter.class);

		bind(
				new TypeLiteral<GenericImporter<es.eucm.eadventure.common.data.animation.Frame, Frame>>() {
				}).to(FrameImporter.class);
		EAdElementFactoryImpl.importerMap.put(
				es.eucm.eadventure.common.data.animation.Frame.class,
				FrameImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Exit, SceneElement>>() {
		}).to(ExitImporter.class);
		EAdElementFactoryImpl.importerMap.put(Exit.class, ExitImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<FlagCondition, OperationCond>>() {
				}).to(FlagConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(
						es.eucm.eadventure.common.data.chapter.conditions.FlagCondition.class,
						FlagConditionImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, OperationCond>>() {
				}).to(VarConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(
						es.eucm.eadventure.common.data.chapter.conditions.VarCondition.class,
						VarConditionImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<ActiveArea, SceneElement>>() {
				}).to(ActiveAreaImporter.class);
		EAdElementFactoryImpl.importerMap.put(ActiveArea.class,
				ActiveAreaImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conversation, Effect>>() {
		}).to(ConversationImporter.class);

		EAdElementFactoryImpl.importerMap.put(Conversation.class,
				ConversationImporter.class);
		EAdElementFactoryImpl.importerMap.put(GraphConversation.class,
				ConversationImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<DialogueConversationNode, Effect>>() {
				}).to(DialogueNodeImporter.class);
		EAdElementFactoryImpl.importerMap.put(DialogueConversationNode.class,
				DialogueNodeImporter.class);

		bind(new TypeLiteral<GenericImporter<ConversationLine, EAdCaption>>() {
		}).to(LineImporterToCaption.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToCaption.class);

		bind(new TypeLiteral<EAdElementImporter<ConversationLine, SpeakEf>>() {
		}).to(LineImporterToShowText.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToShowText.class);

		bind(new TypeLiteral<EAdElementImporter<Timer, Event>>() {
		}).to(TimerImporter.class);
		EAdElementFactoryImpl.importerMap.put(Timer.class, TimerImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Trajectory, NodeTrajectory>>() {
		}).to(TrajectoryImporter.class);
		EAdElementFactoryImpl.importerMap.put(Trajectory.class,
				TrajectoryImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Barrier, SceneElement>>() {
		}).to(BarrierImporter.class);
		EAdElementFactoryImpl.importerMap.put(Barrier.class,
				BarrierImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Book, Scene>>() {
		}).to(BookImporter.class);
		EAdElementFactoryImpl.importerMap.put(Book.class, BookImporter.class);

		bind(ResourceImporter.class).to(ResourceImporterImpl.class);
		bind(EAdElementFactory.class).to(EAdElementFactoryImpl.class);
		bind(ImageLoaderFactory.class).to(ImporterImageLoaderFactory.class);
		bind(InputStreamCreator.class).to(ImporterInputStreamCreator.class);
	}
}
