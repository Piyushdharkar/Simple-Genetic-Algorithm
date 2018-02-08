import java.util.*

fun randomBinary() = Random().nextInt(2)

data class Individual(val chromosome: List<Int> = listOf(1, 1, 1, 1, 1).map { it * randomBinary() })

fun geneticAlgorithm(population: List<Individual>, target: Int, getFitness: (Individual) -> Int, mutations: Int): Individual {

    fun mate(mates: List<Individual>): List<Individual> {
        val randomPointOne = Random().nextInt(mates[0].chromosome.size)
        val randomPointTwo = Random().nextInt(mates[0].chromosome.size)
        val low = if (randomPointOne > randomPointTwo) randomPointTwo else randomPointOne
        val high = if (randomPointOne > randomPointTwo) randomPointOne else randomPointTwo
        val fittest = Individual(mates[0].chromosome.subList(0, low) + mates[1].chromosome.subList(low, high) + mates[0].chromosome.subList(high, mates[1].chromosome.size))
        val secondFittest = Individual(mates[1].chromosome.subList(0, low) + mates[0].chromosome.subList(low, high) + mates[1].chromosome.subList(high, mates[1].chromosome.size))

        return listOf(fittest, secondFittest)
    }

    tailrec fun mutate(individual: Individual, mutations: Int, mutationNo: Int = 0): Individual {
        val chromosome = individual.chromosome
        val mutationPoint = Random().nextInt(chromosome.size)

        return if (mutationNo == mutations - 1)
            Individual(chromosome.subList(0, mutationPoint) + listOf(if (chromosome[mutationPoint] == 1) 0 else 1) + chromosome.subList(mutationPoint + 1, chromosome.size))
        else
            mutate(individual, mutations, mutationNo + 1)
    }

    tailrec fun naturalSelection(population: List<Individual>, target: Int, generation: Int = 0, getFitness: (Individual) -> Int, mutations: Int): Individual {
        val sortedPopulation = population.sortedByDescending { getFitness(it) }

        return if (getFitness(sortedPopulation[0]) == target) {

            println(sortedPopulation.subList(0, 2).map { it.chromosome })

            sortedPopulation[0]
        } else {
            val fittestTwo = sortedPopulation.subList(0, 2)

            println(fittestTwo.map { it.chromosome })

            val matedFittestTwo = mate(fittestTwo)
            val mutatedFittestTwo = matedFittestTwo.map { mutate(it, mutations) }

            val newGeneration = mutatedFittestTwo + sortedPopulation.subList(2, sortedPopulation.size) + mutatedFittestTwo[0]
            naturalSelection(newGeneration, target, generation + 1, getFitness, mutations)
        }
    }

    return naturalSelection(population, target, 0, getFitness, mutations)

}

fun main(args: Array<String>) {
    val population = listOf(Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(),Individual(), Individual(), Individual(), Individual())

    print(geneticAlgorithm(population = population, target = 5, getFitness = { (chromosome) -> chromosome.filter { it == 1 }.size }, mutations = 1))
}