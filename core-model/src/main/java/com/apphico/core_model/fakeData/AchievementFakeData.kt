package com.apphico.core_model.fakeData

import com.apphico.core_model.Achievement
import com.apphico.core_model.CheckListItem
import com.apphico.core_model.Group
import com.apphico.core_model.MeasurementType
import com.apphico.core_model.Progress
import com.apphico.extensions.getNowDate
import com.apphico.extensions.getNowTime

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
                Progress(
                    progress = 78f,
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 76f,
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 74f,
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 71f,
                    date = getNowDate(),
                    time = getNowTime()
                )
            )
        )
    ),
    Achievement(
        name = "Plantar uma árvore",
        endDate = getNowDate(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                Progress(
                    progress = 0.4712f,
                    date = getNowDate(),
                    time = getNowTime(),
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
                Progress(
                    progress = 70f,
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 72f,
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 73f,
                    date = getNowDate(),
                    time = getNowTime()
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
                Progress(
                    progress = 1f,
                    description = "Li O pequeno príncipe",
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 2f,
                    description = "Li Messias de Duna",
                    date = getNowDate(),
                    time = getNowTime()
                )
            )
        )
    ),
    Achievement(
        name = "Terminar de ler a Bíblia",
        endDate = getNowDate(),
        measurementType = MeasurementType.Percentage(
            percentageProgress = listOf(
                Progress(
                    progress = 0.1f,
                    description = "Just beginning",
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 0.4712f,
                    description = "Its getting hard",
                    date = getNowDate(),
                    time = getNowTime()
                ),
                Progress(
                    progress = 1.00f,
                    description = "Finally finished",
                    date = getNowDate(),
                    time = getNowTime()
                )
            )
        )
    )
)