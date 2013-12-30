class Notification < ActiveRecord::Base
  belongs_to :user
  belongs_to :location
  belongs_to :territory
  scope :unread, conditions: {read: false}
end
