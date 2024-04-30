package com.example.btp.utils

import com.example.btp.model.Budget
import com.example.btp.model.Location

fun getSampleDestinations(): List<Location> {
    return listOf(
        Location(
            "Chocolate Hills",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Bohol_Hills%2C_Chocolate_Hills%2C_Philippines.jpg/640px-Bohol_Hills%2C_Chocolate_Hills%2C_Philippines.jpg",
            9.831314,
            124.139503
        ),
        Location(
            "Panglao Island",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Alona_Beach_Palmtree.jpg/1280px-Alona_Beach_Palmtree.jpg",
            9.598647,
            123.811268
        ),
        Location(
            "Loboc River",
            "https://upload.wikimedia.org/wikipedia/commons/5/5c/Loboc_river.png",
            9.722564,
            123.910426
        ),
        Location(
            "Hinagdanan Cave",
            "https://upload.wikimedia.org/wikipedia/commons/8/86/Hinagdanan_cave%2C_Bohol.jpg",
            9.625280,
            123.782451
        ),
        Location(
            "Alona Beach",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Alona_Beach_Overview.jpg/1280px-Alona_Beach_Overview.jpg",
            9.548103,
            123.766754
        ),
        Location(
            "Punta Cruz",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Punta_Cruz_Watchtower_2019.jpg/1280px-Punta_Cruz_Watchtower_2019.jpg",
            9.735042,
            123.787536
        ),
    )
}

fun getSampleBudgets(): List<Budget> {
    return listOf(
        Budget(1000),
        Budget(5000),
        Budget(10000),
        Budget(15000),
        Budget(20000),
        Budget(50000)
    )
}