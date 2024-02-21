package com.apphico.core_model.fakeData

import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementType.Percentage.PercentageProgress
import com.apphico.core_model.MeasurementType.Value.TrackedValues
import java.time.LocalDateTime

val mockedAchievement = Achievement(
    name = "Em busca do shape inexplicável",
    description = "Esse ano eu vou bugar geral.",
    group = mockedGroup,
    endDate = LocalDateTime.now(),
    measurementType = MeasurementType.TaskDone(
        checkList = listOf(
            CheckListItem("Perder 10 kg", false),
            CheckListItem("Ganhar 5kg de massa muscular de puro músculo, ficar grandão", true)
        )
    )
)

val mockedAchievements = listOf(
    mockedAchievement,
    Achievement(
        name = "Emagrecer",
        description = "Ficar fininho igual o seu madruga.",
        endDate = LocalDateTime.now(),
        group = Group(id = 0, name = "Escola", color = -2170138),
        measurementType = MeasurementType.Value(
            startingValue = 80f,
            goalValue = 68f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 78f,
                    date = LocalDateTime.now(),
                ),
                TrackedValues(
                    trackedValue = 76f,
                    date = LocalDateTime.now(),
                ),
                TrackedValues(
                    trackedValue = 74f,
                    date = LocalDateTime.now(),
                ),
                TrackedValues(
                    trackedValue = 71f,
                    date = LocalDateTime.now(),
                )
            )
        )
    ),
    Achievement(
        name = "Plantar uma árvore",
        endDate = LocalDateTime.now(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                PercentageProgress(
                    progress = 0.4712f,
                    date = LocalDateTime.now(),
                    description = "Plantei um pé de feijão."
                )
            )
        )
    ),
    Achievement(
        name = "Ganhar peso",
        description = "Birlllll, ta saindo da jaula o monstro.  Eu treino pra ficar estranho mesmo, se fosse pra ficar bonito eu ia pro salão.",
        group = Group(id = 0, name = "Escola", color = -12677526),
        endDate = LocalDateTime.now(),
        measurementType = MeasurementType.Value(
            startingValue = 68f,
            goalValue = 85f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 70f,
                    date = LocalDateTime.now()
                ),
                TrackedValues(
                    trackedValue = 72f,
                    date = LocalDateTime.now()
                ),
                TrackedValues(
                    trackedValue = 73f,
                    date = LocalDateTime.now()
                )
            )
        )
    ),
    Achievement(
        name = "Ser a pessoa mais sensacional existente no planeta Terra",
        endDate = LocalDateTime.now(),
        doneDate = LocalDateTime.now(),
    ),
    Achievement(
        name = "Ler 30 livros no ano",
        endDate = LocalDateTime.now(),
        group = Group(id = 0, name = "Escola", color = -7745552),
        measurementType = MeasurementType.Value(
            startingValue = 0f,
            goalValue = 30f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 1f,
                    date = LocalDateTime.now(),
                    description = "Li O pequeno príncipe"
                ),
                TrackedValues(
                    trackedValue = 2f,
                    date = LocalDateTime.now(),
                    description = "Li Messias de Duna"
                )
            )
        )
    ),
    Achievement(
        name = "Terminar de ler a Bíblia",
        endDate = LocalDateTime.now(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                PercentageProgress(
                    progress = 0.1f,
                    date = LocalDateTime.now(),
                    description = "Just beginning"
                ),
                PercentageProgress(
                    progress = 0.4712f,
                    date = LocalDateTime.now(),
                    description = "Its getting hard"
                ),
                PercentageProgress(
                    progress = 1.00f,
                    date = LocalDateTime.now(),
                    description = "Finally finished"
                )
            )
        )
    )
)