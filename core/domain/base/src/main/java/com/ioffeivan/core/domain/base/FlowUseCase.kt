package com.ioffeivan.core.domain.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for all Flow-based UseCases in the Domain layer.
 * Used for operations that return a stream of data.
 * Ensures that the flow execution and transformations happen on the correct [dispatcher].
 *
 * @param Parameters Input data for the UseCase.
 * @param Success Expected output data on success.
 * @param BusinessRuleError Expected domain-specific error.
 */
abstract class FlowUseCase<in Parameters, Success, BusinessRuleError>(
    private val dispatcher: CoroutineDispatcher,
) {
    /**
     * Executes the flow-based business logic and handles unexpected exceptions.
     * Uses [flowOn] to ensure the upstream work is performed on the designated [dispatcher].
     * @return A [Flow] emitting [Result] states.
     */
    operator fun invoke(parameters: Parameters): Flow<Result<Success, BusinessRuleError>> {
        return execute(parameters)
            .flowOn(dispatcher)
    }

    /**
     * Implementation of the reactive business logic.
     */
    protected abstract fun execute(parameters: Parameters): Flow<Result<Success, BusinessRuleError>>
}
