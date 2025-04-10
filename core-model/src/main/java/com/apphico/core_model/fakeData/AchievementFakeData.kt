package com.apphico.core_model.fakeData

import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.MeasurementType.Percentage.PercentageProgress
import com.apphico.core_model.MeasurementType.Value.TrackedValues
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowDateTime

val mockedAchievement = Achievement(
    name = "Em busca do shape inexplicável",
    description = "Esse ano eu vou bugar geral.",
    group = mockedGroup,
    endDate = getNowDate(),
    measurementType = MeasurementType.TaskDone(
        checkList = listOf(
            CheckListItem(0L, "Perder 10 kg", false),
            CheckListItem(0L, "Ganhar 5kg de massa muscular de puro músculo, ficar grandão", true)
        )
    )
)

val mockedAchievements = listOf(
    mockedAchievement,
    Achievement(
        name = "Emagrecer",
        description = "Ficar fininho igual o seu madruga.",
        endDate = getNowDate(),
        group = Group(id = 0, name = "Escola", color = -2170138),
        measurementType = MeasurementType.Value(
            startingValue = 80f,
            goalValue = 68f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 78f,
                    date = getNowDateTime(),
                ),
                TrackedValues(
                    trackedValue = 76f,
                    date = getNowDateTime(),
                ),
                TrackedValues(
                    trackedValue = 74f,
                    date = getNowDateTime(),
                ),
                TrackedValues(
                    trackedValue = 71f,
                    date = getNowDateTime(),
                )
            )
        )
    ),
    Achievement(
        name = "Plantar uma árvore",
        endDate = getNowDate(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                PercentageProgress(
                    progress = 0.4712f,
                    date = getNowDateTime(),
                    description = "Plantei um pé de feijão."
                )
            )
        )
    ),
    Achievement(
        name = "Ganhar peso",
        description = "Birlllll, ta saindo da jaula o monstro.  Eu treino pra ficar estranho mesmo, se fosse pra ficar bonito eu ia pro salão.",
        group = Group(id = 0, name = "Escola", color = -12677526),
        endDate = getNowDate(),
        measurementType = MeasurementType.Value(
            startingValue = 68f,
            goalValue = 85f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 70f,
                    date = getNowDateTime()
                ),
                TrackedValues(
                    trackedValue = 72f,
                    date = getNowDateTime()
                ),
                TrackedValues(
                    trackedValue = 73f,
                    date = getNowDateTime()
                )
            )
        )
    ),
    Achievement(
        name = "Ser a pessoa mais sensacional existente no planeta Terra",
        endDate = getNowDate(),
        doneDate = getNowDate(),
    ),
    Achievement(
        name = "Ler 30 livros no ano",
        endDate = getNowDate(),
        group = Group(id = 0, name = "Escola", color = -7745552),
        measurementType = MeasurementType.Value(
            startingValue = 0f,
            goalValue = 30f,
            trackedValues = listOf(
                TrackedValues(
                    trackedValue = 1f,
                    date = getNowDateTime(),
                    description = "Li O pequeno príncipe"
                ),
                TrackedValues(
                    trackedValue = 2f,
                    date = getNowDateTime(),
                    description = "Li Messias de Duna"
                )
            )
        )
    ),
    Achievement(
        name = "Terminar de ler a Bíblia",
        endDate = getNowDate(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                PercentageProgress(
                    progress = 0.1f,
                    date = getNowDateTime(),
                    description = "Just beginning"
                ),
                PercentageProgress(
                    progress = 0.4712f,
                    date = getNowDateTime(),
                    description = "Its getting hard"
                ),
                PercentageProgress(
                    progress = 1.00f,
                    date = getNowDateTime(),
                    description = "Finally finished"
                )
            )
        )
    )
)