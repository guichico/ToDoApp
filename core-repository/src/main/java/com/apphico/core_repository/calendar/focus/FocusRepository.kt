package com.apphico.core_repository.calendar.focus

import com.apphico.core_model.FocusMode
import com.apphico.core_model.fakeData.mockedGroups
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FocusRepository {
    fun getRoutines(): Flow<List<FocusMode>>
}

class FocusRepositoryImpl : FocusRepository {

    override fun getRoutines(): Flow<List<FocusMode>> =
        flow {
            emit(
                listOf(
                    FocusMode(
                        name = "Trabalho",
                        timer = 3000000,
                        interval = 300000,
                        group = mockedGroups[2]
                    ),
                    FocusMode(
                        name = "Estudos",
                        timer = 6000000,
                        interval = 1800000,
                        group = mockedGroups[1]
                    ),
                    FocusMode(
                        name = "Exercícios",
                        timer = 300000,
                        interval = 60000,
                        group = mockedGroups.first()
                    )
                )
            )
        }
}