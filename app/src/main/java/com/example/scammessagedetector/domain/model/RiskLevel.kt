package com.example.scammessagedetector.domain.model

// Enum representing the risk level of a message
// SAFE: Message appears to be legitimate and safe
// SUSPICIOUS: Message has some warning signs but not conclusive
// DANGEROUS: Message has multiple red flags typical of scams or phishing
enum class RiskLevel {
    SAFE,
    SUSPICIOUS,
    DANGEROUS
}