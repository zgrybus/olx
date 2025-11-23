import { Card, CardContent } from '@/components/ui/card';
import { dateTimeFormatUtil } from '@/features/date-utils/date-time-format-util';
import { priceFormatUtil } from '@/features/number-utils/number-format-util';

const dateTimeFormat = dateTimeFormatUtil({
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

type OfferDetailsSummaryCardProps = {
  title: string | undefined;
  price: number | undefined;
  createdAt: string | undefined;
};

export const OfferDetailsSummaryCard = ({
  createdAt,
  title,
  price,
}: OfferDetailsSummaryCardProps) => {
  return (
    <Card>
      <CardContent
        className="space-y-4"
        data-testid="offer-details-summary-card"
      >
        <div>
          {createdAt && (
            <span className="text-xs text-neutral-500">
              Added {dateTimeFormat.format(new Date(createdAt))}
            </span>
          )}
        </div>
        <h1 className="text-2xl leading-snug font-medium text-neutral-900">
          {title}
        </h1>
        {price && (
          <div className="text-3xl font-bold text-neutral-900">
            {priceFormatUtil.format(price)}
          </div>
        )}
      </CardContent>
    </Card>
  );
};
