import { dateTimeFormatUtil } from '@/features/date-utils/date-time-format-util';
import { priceFormatUtil } from '@/features/number-utils/number-format-util';

const dateTimeFormat = dateTimeFormatUtil({
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

type OfferItemProps = {
  title: string;
  price: string;
  createdAt: string;
  updatedAt: string;
  description: string;
};

export function OfferItem({
  title,
  price,
  updatedAt,
  createdAt,
  description,
}: OfferItemProps) {
  return (
    <li className="bg-white p-2 h-[120px] shadow-sm flex flex-col justify-between font-normal md:h-40">
      <div className="flex justify-between gap-3">
        <h3 className="text-base font-medium line-clamp-2" title={title}>
          {title}
        </h3>
        <p className="text-base font-bold whitespace-nowrap shrink-0">
          {priceFormatUtil.format(parseFloat(price))}
        </p>
      </div>
      <div className="flex-1 min-h-0 overflow-hidden relative mt-5 mb-3 hidden md:block">
        <p className="text-sm text-neutral-600 line-clamp-2">{description}</p>
      </div>
      <div className="text-xs text-neutral-400 mt-auto pt-2">
        <p>
          <span className="font-bold">Updated at</span>{' '}
          {dateTimeFormat.format(new Date(updatedAt))}
        </p>
        <p>
          <span className="font-bold">Created at</span>{' '}
          {dateTimeFormat.format(new Date(createdAt))}
        </p>
      </div>
    </li>
  );
}
