package lotto.domain

import lotto.domain.dto.LottoMatchResult
import lotto.domain.dto.LottoNumber
import lotto.domain.dto.Rank

class LottoDrawResult(private val luckyDrawNumber: List<Int>, private val bonusNumber: Int) {
    var result: Long = 0
        private set
    var three: Int = 0
        private set
    var four: Int = 0
        private set
    var five: Int = 0
        private set
    var fiveWithBonus: Int = 0
        private set
    var six = 0
        private set
    private var none: Int = 0

    fun draw(lottoList: List<LottoNumber>) {
        lottoList.forEach {
            this.plusCount(checkDrawResult(it))
        }
    }

    private fun checkDrawResult(lottoNumber: LottoNumber): LottoMatchResult {
        val numbers = lottoNumber.numbers
        return LottoMatchResult(
            luckyDrawNumber.count { numbers.contains(it) },
            numbers.contains(bonusNumber)
        )
    }

    private fun plusCount(lottoMatchResult: LottoMatchResult) {
        when (lottoMatchResult.matchCount) {
            Rank.FIFTH.count -> {
                three++
                this.result.add(Rank.FIFTH)
            }
            Rank.FOURTH.count -> {
                four++
                this.result.add(Rank.FOURTH)
            }
            Rank.SECOND.count -> {
                five++
                this.result.addSecondOrThirdAmount(lottoMatchResult)
            }
            Rank.FIRST.count -> {
                six++
                this.result.add(Rank.FIRST)
            }
            else -> none++
        }
    }

    private fun Long.addSecondOrThirdAmount(lottoMatchResult: LottoMatchResult) {
        if (lottoMatchResult.isBonusMatch) {
            fiveWithBonus++
            this.add(Rank.SECOND)
        } else {
            five++
            this.add(Rank.THIRD)
        }
    }

    private fun Long.add(drawAmount: Rank) {
        this + drawAmount.amount
    }
}
