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
import es.eucm.eadventure.common.data.chapter.Timer;
import es.eucm.eadventure.common.data.chapter.Trajectory;
import es.eucm.eadventure.common.data.chapter.book.Book;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
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
import es.eucm.eadventure.common.impl.importer.auxiliar.EAPInputStreamCreator;
import es.eucm.eadventure.common.impl.importer.auxiliar.EAdElementFactoryImpl;
import es.eucm.eadventure.common.impl.importer.auxiliar.ImporterImageLoaderFactory;
import es.eucm.eadventure.common.impl.importer.auxiliar.ZipInputStreamCreator;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.impl.importer.resources.AnimationImporter;
import es.eucm.eadventure.common.impl.importer.resources.FrameImporter;
import es.eucm.eadventure.common.impl.importer.resources.ResourceImporterImpl;
import es.eucm.eadventure.common.impl.importer.subimporters.AdventureImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.books.BookImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ActionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.AtrezzoImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ChapterImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.ItemImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.NPCImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.TimerImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.ConversationImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.DialogueNodeImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.LineImporterToCaption;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.conversations.LineImporterToShowText;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.cutscene.SlidesceneImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.cutscene.VideosceneImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ActiveAreaImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.BarrierImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ElementReferenceImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.ExitImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.SceneImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.TrajectoryImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.ConditionsImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.FlagConditionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.conditions.VarConditionImporter;
import es.eucm.eadventure.common.impl.importer.subimporters.effects.EffectsImporterModule;
import es.eucm.eadventure.common.impl.importer.subimporters.macros.MacroImporter;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.conditions.impl.FlagCondition;
import es.eucm.eadventure.common.model.conditions.impl.VarCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.EAdMacro;
import es.eucm.eadventure.common.model.effects.impl.EAdTriggerMacro;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.EAdAdventureModel;
import es.eucm.eadventure.common.model.elements.EAdChapter;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.EAdTimer;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdVideoScene;
import es.eucm.eadventure.common.model.elements.impl.extra.EAdCutscene;
import es.eucm.eadventure.common.model.trajectories.impl.NodeTrajectoryDefinition;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

/**
 * Guice module to configure the <e-Adventure> 1.X game importer
 */
public class ImporterConfigurationModule extends AbstractModule {

	private String projectFile;

	/**
	 * 
	 * @param projectFile
	 *            Could be an *.eap file or and *.ead file
	 */
	public ImporterConfigurationModule(String projectFile) {
		this.projectFile = projectFile;
	}

	@Override
	protected void configure() {
		this.install(new EffectsImporterModule());

		bind(String.class).annotatedWith(Names.named("projectFolder"))
				.toInstance(projectFile);
		bind(String.class).annotatedWith(Names.named("StringsFile"))
				.toInstance(projectFile + "/strings.xml");

		bind(
				new TypeLiteral<EAdElementImporter<AdventureData, EAdAdventureModel>>() {
				}).to(AdventureImporter.class);
		EAdElementFactoryImpl.importerMap.put(AdventureData.class,
				AdventureImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Chapter, EAdChapter>>() {
		}).to(ChapterImporter.class);
		EAdElementFactoryImpl.importerMap.put(Chapter.class,
				ChapterImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Scene, EAdSceneImpl>>() {
		}).to(SceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Scene.class, SceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Slidescene, EAdCutscene>>() {
		}).to(SlidesceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Slidescene.class,
				SlidesceneImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Videoscene, EAdVideoScene>>() {
		}).to(VideosceneImporter.class);
		EAdElementFactoryImpl.importerMap.put(Videoscene.class,
				VideosceneImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<ElementReference, EAdActorReference>>() {
				}).to(ElementReferenceImporter.class);
		EAdElementFactoryImpl.importerMap.put(ElementReference.class,
				ElementReferenceImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Atrezzo, EAdActor>>() {
		}).to(AtrezzoImporter.class);
		EAdElementFactoryImpl.importerMap.put(Atrezzo.class,
				AtrezzoImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Item, EAdActor>>() {
		}).to(ItemImporter.class);
		EAdElementFactoryImpl.importerMap.put(Item.class, ItemImporter.class);

		bind(new TypeLiteral<EAdElementImporter<NPC, EAdActor>>() {
		}).to(NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(NPC.class, NPCImporter.class);
		EAdElementFactoryImpl.importerMap.put(Player.class, NPCImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conditions, EAdCondition>>() {
		}).to(ConditionsImporter.class);
		EAdElementFactoryImpl.importerMap.put(Conditions.class,
				ConditionsImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Macro, EAdMacro>>() {
		}).to(MacroImporter.class);
		EAdElementFactoryImpl.importerMap.put(Macro.class, MacroImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Action, EAdAction>>() {
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

		bind(new TypeLiteral<EAdElementImporter<Exit, EAdSceneElement>>() {
		}).to(ExitImporter.class);
		EAdElementFactoryImpl.importerMap.put(Exit.class, ExitImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.FlagCondition, FlagCondition>>() {
				}).to(FlagConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(es.eucm.eadventure.common.data.chapter.conditions.FlagCondition.class,
						FlagConditionImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<es.eucm.eadventure.common.data.chapter.conditions.VarCondition, VarCondition>>() {
				}).to(VarConditionImporter.class);
		EAdElementFactoryImpl.importerMap
				.put(es.eucm.eadventure.common.data.chapter.conditions.VarCondition.class,
						VarConditionImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<ActiveArea, EAdSceneElement>>() {
				}).to(ActiveAreaImporter.class);
		EAdElementFactoryImpl.importerMap.put(ActiveArea.class,
				ActiveAreaImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Conversation, EAdEffect>>() {
		}).to(ConversationImporter.class);

		EAdElementFactoryImpl.importerMap.put(Conversation.class,
				ConversationImporter.class);
		EAdElementFactoryImpl.importerMap.put(GraphConversation.class,
				ConversationImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<DialogueConversationNode, EAdTriggerMacro>>() {
				}).to(DialogueNodeImporter.class);
		EAdElementFactoryImpl.importerMap.put(DialogueConversationNode.class,
				DialogueNodeImporter.class);

		bind(new TypeLiteral<GenericImporter<ConversationLine, Caption>>() {
		}).to(LineImporterToCaption.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToCaption.class);

		bind(
				new TypeLiteral<EAdElementImporter<ConversationLine, EAdShowSceneElement>>() {
				}).to(LineImporterToShowText.class);
		EAdElementFactoryImpl.importerMap.put(ConversationLine.class,
				LineImporterToShowText.class);

		bind(new TypeLiteral<EAdElementImporter<Timer, EAdTimer>>() {
		}).to(TimerImporter.class);
		EAdElementFactoryImpl.importerMap.put(Timer.class, TimerImporter.class);

		bind(
				new TypeLiteral<EAdElementImporter<Trajectory, NodeTrajectoryDefinition>>() {
				}).to(TrajectoryImporter.class);
		EAdElementFactoryImpl.importerMap.put(Trajectory.class,
				TrajectoryImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Barrier, EAdSceneElement>>() {
		}).to(BarrierImporter.class);
		EAdElementFactoryImpl.importerMap.put(Barrier.class,
				BarrierImporter.class);

		bind(new TypeLiteral<EAdElementImporter<Book, EAdScene>>() {
		}).to(BookImporter.class);
		EAdElementFactoryImpl.importerMap.put(Book.class, BookImporter.class);

		bind(ResourceImporter.class).to(ResourceImporterImpl.class);
		bind(EAdElementFactory.class).to(EAdElementFactoryImpl.class);
		bind(ImageLoaderFactory.class).to(ImporterImageLoaderFactory.class);

		if (projectFile.endsWith(".eap")) {
			projectFile = projectFile.substring(0, projectFile.length() - 4);
			bind(InputStreamCreator.class).toInstance(
					new EAPInputStreamCreator(projectFile));
		} else if (projectFile.endsWith(".zip") || projectFile.endsWith(".ead")) {
			bind(InputStreamCreator.class).toInstance(
					new ZipInputStreamCreator(projectFile));
		}
	}

}
