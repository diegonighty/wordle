package com.github.diegonighty.wordle.concurrent;

import com.github.diegonighty.wordle.concurrent.bukkit.BukkitExecutorProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Execute a task in Bukkit Main Thread or in a FixedThreadPool
 * @param <T> ObjectType involved in this promise
 */
public final class Promise<T> {

	private final static Executor ASYNC_EXECUTOR = Executors.newFixedThreadPool(3);
	private final static Executor MAIN_EXECUTOR = BukkitExecutorProvider.get();

	private final CompletableFuture<T> backingFuture;

	private Consumer<Throwable> catchAction = Throwable::printStackTrace;
	private Consumer<T> completeAction = t -> {};

	private Promise(CompletableFuture<T> future) {
		this.backingFuture = future;
		updateBacking();
	}

	/**
	 * Create a new promise from CompletableFuture
	 * @param future A CompletableFuture to be converted in a new Promise
	 * @param <V>    ObjectType involved in this promise
	 * @return a new Promise with the CompletableFuture action
	 */
	public static <V> Promise<V> from(CompletableFuture<V> future) {
		requireNonNull(future, "Future cant be null");

		return new Promise<>(future);
	}

	/**
	 * Create a new async promise
	 * @param supplier async task
	 * @param <V>      ObjectType involved in this promise
	 * @return a new Promise
	 */
	public static <V> Promise<V> supplyAsync(Supplier<V> supplier) {
		requireNonNull(supplier, "Supplier cant be null");

		return new Promise<>(
				CompletableFuture.supplyAsync(supplier, ASYNC_EXECUTOR)
		);
	}

	/**
	 * Create a new async promise
	 * @param runnable async task
	 * @return a new Promise
	 */
	public static Promise<Void> runAsync(Runnable runnable) {
		requireNonNull(runnable, "Runnable cant be null");

		return new Promise<>(
				CompletableFuture.runAsync(runnable, ASYNC_EXECUTOR)
		);
	}

	/**
	 * Create a new bukkit main thread promise
	 * @param supplier main thread task
	 * @param <V>      ObjectType involved in this promise
	 * @return a new Promise
	 */
	public static <V> Promise<V> supply(Supplier<V> supplier) {
		requireNonNull(supplier, "Supplier cant be null");

		return new Promise<>(
				CompletableFuture.supplyAsync(supplier, MAIN_EXECUTOR)
		);
	}

	/**
	 * Create a new bukkit main thread promise
	 * @param runnable main thread task
	 * @return a new Promise
	 */
	public static Promise<Void> run(Runnable runnable) {
		requireNonNull(runnable, "Runnable cant be null");

		return new Promise<>(
				CompletableFuture.runAsync(runnable, MAIN_EXECUTOR)
		);
	}

	public static <V> Promise<V> withCompleted(V result) {
		requireNonNull(result, "Result cant be null");

		return new Promise<>(
			CompletableFuture.completedFuture(result)
		);
	}

	/**
	 * A task to do when the promise is completed
	 * @param completeConsumer action
	 * @return the promise with the changes
	 */
	public Promise<T> onComplete(Consumer<T> completeConsumer) {
		this.completeAction = completeConsumer;

		return this;
	}

	/**
	 * A task to do ONLY IF the promise is failed
	 * @param throwableConsumer failure action
	 * @return the promise with the changes
	 */
	public Promise<T> onFailure(Consumer<Throwable> throwableConsumer) {
		this.catchAction = throwableConsumer;

		return this;
	}

	private void updateBacking() {
		backingFuture.whenComplete((t, throwable) -> {
			if (throwable != null) {
				catchAction.accept(throwable);
			} else {
				completeAction.accept(t);
			}
		});
	}

	public CompletableFuture<T> toCompletable() {
		return backingFuture;
	}

}
