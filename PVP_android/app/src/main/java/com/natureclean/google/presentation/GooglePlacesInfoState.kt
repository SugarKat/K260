package com.natureclean.google.presentation

import com.natureclean.google.domain.model.GooglePlacesInfo

data class GooglePlacesInfoState(val direction: GooglePlacesInfo? = null, val isLoading: Boolean = false)