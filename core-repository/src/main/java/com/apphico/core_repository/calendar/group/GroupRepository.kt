package com.apphico.core_repository.calendar.group

import com.apphico.core_model.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GroupRepository {

    fun getGroups(): Flow<List<Group>>

}

class GroupRepositoryImpl : GroupRepository {

    override fun getGroups(): Flow<List<Group>> =
        flow {
            emit(
                listOf(
                    Group(
                        id = 0,
                        name = "Saúde",
                        color = -7745552
                    ),
                    Group(
                        id = 0,
                        name = "Trabalho",
                        color = -10215
                    ),
                    Group(
                        id = 0,
                        name = "Família",
                        color = -12677526
                    ),
                    Group(
                        id = 0,
                        name = "Escola",
                        color = -883456
                    ),
                    Group(
                        id = 0,
                        name = "Tarefas domésticas",
                        color = -19514
                    ),


                    Group(
                        id = 0,
                        name = "Eventos",
                        color = -1886900
                    ),
                    Group(
                        id = 0,
                        name = "Criatividade",
                        color = -6323856
                    ),
                    Group(
                        id = 0,
                        name = "Times",
                        color = -2170138
                    )
                )
            )
        }
}